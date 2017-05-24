package com.siebre.payment.utils.messageconvert;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
public class MapEntryConverter
  implements Converter
{
  public boolean canConvert(Class clazz) { return AbstractMap.class.isAssignableFrom(clazz); }
  
  public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
   AbstractMap map = (AbstractMap)value;
    for (Iterator localIterator = map.entrySet().iterator(); localIterator.hasNext();) { Object obj = localIterator.next();
      Map.Entry entry = (Map.Entry)obj;
     writer.startNode(entry.getKey().toString());
     writer.setValue(entry.getValue() == null ? "" : entry.getValue().toString());
     writer.endNode();
   }
 }

  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) { Map<String, String> map = new HashMap();
   while (reader.hasMoreChildren()) {
     reader.moveDown();
    map.put(reader.getNodeName(), reader.getValue());
      reader.moveUp();
   }
   return map;
 }
}
