package com.siebre.agreement.io.support;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.castor.CastorMarshaller;

import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author ZhangChi
 * @since 2013-3-31
 */
abstract class DefaultMarshallers {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultMarshallers.class);

    private static final String SMF_CASTOR_MAPPING_FILE = "SmfCastorMapping.xml";

    //TODO consider using jackson xml mapping

    private static final CastorMarshaller DEFAULT_AGREEMENTSPEC_MARSHALLER = createAgreementSpecMarshaller(SMF_CASTOR_MAPPING_FILE);

    private static RuntimeException INIT_EXCEPTION;

    public static Unmarshaller agreementSpecUnmarshaller() {
        if (DEFAULT_AGREEMENTSPEC_MARSHALLER == null) {
            throw INIT_EXCEPTION;
        }
        return DEFAULT_AGREEMENTSPEC_MARSHALLER;
    }

    //TODO move to marshaller builder
    public static Unmarshaller agreementSpecUnmarshaller(String castorMappingFilePath) {
        return createAgreementSpecMarshaller(castorMappingFilePath);//TODO add cache
    }

    public static Marshaller agreementSpecMarshaller() {
        if (DEFAULT_AGREEMENTSPEC_MARSHALLER == null) {
            throw INIT_EXCEPTION;
        }
        return DEFAULT_AGREEMENTSPEC_MARSHALLER;
    }

    private static CastorMarshaller createAgreementSpecMarshaller(String mappingLocation) {
        CastorMarshaller result = new CastorMarshaller() {

            private ThreadLocal<Writer> writerOfThread = new ThreadLocal<Writer>();

            @Override
            protected void marshalStreamResult(Object graph, StreamResult streamResult) throws XmlMappingException, IOException {

                try {
                    if (streamResult.getOutputStream() != null) {
                        OutputStreamWriter writer = new OutputStreamWriter(streamResult.getOutputStream(), DEFAULT_ENCODING);
                        writerOfThread.set(writer);
                        marshalWriter(graph, writer);
                    } else if (streamResult.getWriter() != null) {
                        writerOfThread.set(streamResult.getWriter());
                        marshalWriter(graph, streamResult.getWriter());
                    } else {
                        throw new IllegalArgumentException("StreamResult contains neither OutputStream nor Writer");
                    }
                } finally {
                    writerOfThread.remove();
                }
            }

            @Override
            protected void customizeMarshaller(org.exolab.castor.xml.Marshaller marshaller) {
                super.customizeMarshaller(marshaller);
                if (writerOfThread.get() != null) {

                    OutputFormat outputFormat = new OutputFormat("xml", DEFAULT_ENCODING, true);
                    outputFormat.setCDataElements(new String[] {"body", "commentOnFailure"});
                    try {
                        marshaller.setDocumentHandler(new XMLSerializer(writerOfThread.get(), outputFormat).asDocumentHandler());
//						marshaller.setContentHandler(new XMLSerializer(writerOfThread.get(), outputFormat).asContentHandler());
                    } catch (IOException e) {
                        throw Throwables.propagate(e);
                    }
                }
            }
        };
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        result.setMappingLocation(resourceLoader.getResource(mappingLocation));
        result.setIgnoreExtraElements(true);
        //TODO externalize it to builder
        result.setCastorProperties(ImmutableMap.of("org.exolab.castor.indent", "true"));
        try {
            result.afterPropertiesSet();
        } catch (Exception e) {
            LOGGER.error("Failed to create default marshaller/unmarshaller for AgreementSpec", e);
            result = null;
            if (e instanceof RuntimeException) {
                INIT_EXCEPTION = (RuntimeException) e;
            } else {
                INIT_EXCEPTION = new RuntimeException(e);
            }
        }

        result.setEncoding("GBK");

        return result;
    }

}
