package com.siebre.payment.billing.util;

import com.siebre.payment.billing.entity.ReconDataField;
import com.siebre.payment.billing.entity.ReconDataSet;
import com.siebre.payment.utils.JsonUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by tianci.huang on 2017/6/29.
 */
public class MatchCriteriaEngineTest {

    public String exp1 = "#1.REQ_SN=#2.external_order_number";
    public String exp3 = "#1.AMOUNT=#2.amount";
    public String exp5 = "#1.STATUS=0000";

    public String exp2 = "#1.STATUS=0000 and (#2.status=3 or #2.status=10)";
    public String exp4 = "(#2.status=3 or #2.status=10) and #1.STATUS=0000";

    public JsonNode remoteJN;
    public JsonNode localJN;
    public List<ReconDataField> remoteDataFields;
    public List<ReconDataField> localDataFields;

    @Before
    public void before() {
        remoteDataFields = new ArrayList<>();
        remoteDataFields.add(reconDataField("REQ_SN", "String"));
        remoteDataFields.add(reconDataField("STATUS", "String"));
        remoteDataFields.add(reconDataField("AMOUNT", "Currency(Fen)"));
        localDataFields = new ArrayList<>();
        localDataFields.add(reconDataField("external_order_number", "String"));
        localDataFields.add(reconDataField("status", "Integer"));
        localDataFields.add(reconDataField("amount", "BigDecimal"));
        Map<String, Object> remote = new HashedMap();
        remote.put("REQ_SN", "200368000000961-1498631175845");
        remote.put("STATUS", "0000");
        remote.put("AMOUNT", "0.01");
        remoteJN = toJsonNode(remoteDataFields, remote);
        Map<String, Object> local = new HashedMap();
        local.put("external_order_number", "200368000000961-1498631175845");
        local.put("status", "3");
        local.put("amount", "0.01");
        localJN = toJsonNode(localDataFields, local);

    }

    public ReconDataField reconDataField(String name, String type) {
        ReconDataField reconDataField = new ReconDataField();
        reconDataField.setName(name);
        reconDataField.setType(type);
        return  reconDataField;
    }

    @Test
    public void match() throws Exception {
        Assert.assertTrue(MatchCriteriaEngine.match(exp2, remoteJN, localJN, remoteDataFields, localDataFields));
        Assert.assertTrue(MatchCriteriaEngine.match(exp4, remoteJN, localJN, remoteDataFields, localDataFields));
    }

    @Test
    public void matchWithOutBracket() throws Exception {
        Assert.assertTrue(MatchCriteriaEngine.matchWithOutBracket(exp1, remoteJN, localJN, remoteDataFields, localDataFields));
        Assert.assertTrue(MatchCriteriaEngine.matchWithOutBracket(exp3, remoteJN, localJN, remoteDataFields, localDataFields));
        Assert.assertTrue(MatchCriteriaEngine.matchWithOutBracket(exp5, remoteJN, localJN, remoteDataFields, localDataFields));
    }

    private String toJsonStr(List<ReconDataField> fields, Map<String, Object> rs) {
        ObjectMapper mapper = JsonUtils.MAPPER;
        ObjectNode rootNode = mapper.createObjectNode();
        StringWriter writer = new StringWriter();

        try {
            for (ReconDataField field : fields) {
                String name = field.getName();
                Object value = rs.get(name);
                if (value != null)
                    rootNode.put(name, value.toString());
                else
                    rootNode.put(name, "");
            }
            mapper.writeValue(writer, rootNode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return writer.toString();
    }

    private JsonNode toJsonNode(List<ReconDataField> fields, Map<String, Object> rs) {
        String json = toJsonStr(fields, rs);
        JsonNode node = null;

        if (StringUtils.isBlank(json))
            return node;

        try {
            node = JsonUtils.getJsonNode(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }

}