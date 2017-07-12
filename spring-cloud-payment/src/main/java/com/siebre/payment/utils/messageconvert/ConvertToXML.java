package com.siebre.payment.utils.messageconvert;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConvertToXML {
    private static Logger logger= LoggerFactory.getLogger(ConvertToXML.class);

	public static String toXml(Object object) {
		XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xstream.alias("xml", object.getClass());
		xstream.alias("xml", Map.class);
		xstream.registerConverter(new MapEntryConverter());
		return xstream.toXML(object);
	}

	public static <T> T toObject(String xml, Class<T> cls) {
		XStream xstream = new XStream(new DomDriver("UTF-8"));
		xstream.alias("xml", cls);
		xstream.alias("xml", Map.class);
		xstream.registerConverter(new MapEntryConverter());
		xstream.ignoreUnknownElements();
		return (T) xstream.fromXML(xml);
	}

	public static Map<String, String> toMap(String xml) {
		return toMap(xml, "UTF-8");
	}

	public static Map<String, String> toMap(String xml, String charSetName) {
		Map<String, String> map = new HashMap();
		if ((xml == null) || (xml.trim().length() == 0)) {
			return map;
		}

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new ByteArrayInputStream(xml.getBytes(charSetName)));
			Element root = document.getDocumentElement();
			NodeList nodeList = root.getChildNodes();
			int length = nodeList.getLength();
			for (int i = 0; i < length; i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == 1) {
					NodeList valueNode = node.getChildNodes();
					Object value = null;
					if (valueNode.getLength() > 0) {
						value = valueNode.item(0).getNodeValue();
					}
					map.put(node.getNodeName(), value == null ? "" : value.toString());
				}
			}
		} catch (Exception e) {
			logger.error("xml转换map失败!" + xml, e);
		}
		return map;
	}

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap();
		map.put("name", "admin");
		map.put("date", new Date());

		String xml = toXml(map);
		System.out.println(xml);

		Map r = (Map) toObject(xml, Map.class);

		System.out.println(r);
	}


}
