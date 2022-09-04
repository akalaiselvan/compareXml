import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompareXml {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder=factory.newDocumentBuilder();


        Document sourceDoc=documentBuilder.parse(new File("source.xml"));
        Document targetDoc=documentBuilder.parse(new File("target.xml"));
        sourceDoc.getDocumentElement().normalize();
        targetDoc.getDocumentElement().normalize();


        NodeList sourceList=sourceDoc.getElementsByTagName("*");
        NodeList targetList=targetDoc.getElementsByTagName("*");

        List<String> source=getTags(sourceList);
        List<String> target=getTags(targetList);

        System.out.println(compareTags(source,target));

    }

    @SuppressWarnings("unchecked")
    private static JSONObject compareTags(List<String> source, List<String> target) {
        JSONObject jsonObject=new JSONObject();
        int newTags=0;
        JSONArray addedtags=new JSONArray();

        int sourceCount=0;
        for (String targetTag : target) {
            if (source.get(sourceCount).equalsIgnoreCase(targetTag)) {
                if (sourceCount<source.size()-1){
                    sourceCount++;
                }else {
                    sourceCount=source.size()-1;
                }

            } else {
                addedtags.add(targetTag);
                newTags++;
            }
        }

        jsonObject.put("sourceTagCount",source.size());
        jsonObject.put("targetTagCount",target.size());
        jsonObject.put("newTagsinTarget",newTags);
        jsonObject.put("addedtags",addedtags);

        return jsonObject;
    }

    private static List<String> getTags(NodeList list){
        List<String> tags=new ArrayList<String>();
        for (int i=0;i<list.getLength();i++){
            Node node=list.item(i);
            if (node.getNodeType()==Node.ELEMENT_NODE){
                tags.add(node.getNodeName());
            }
        }
        return tags;
    }
}
