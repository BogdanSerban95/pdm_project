package project.passwordproject.classes;

import android.content.Context;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Serban on 13/12/2016.
 */

public class Utilities {
    public static String createXml(List<Site> siteList) throws IOException {

        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        serializer.setOutput(writer);

        serializer.startDocument("UTF-8", true);
        serializer.startTag(null, "sites");

        for (Site site : siteList) {
            serializer.startTag(null, "site");

            serializer.startTag(null, "name");
            serializer.text(site.getName());
            serializer.endTag(null, "name");

            serializer.startTag(null, "address");
            serializer.text(site.getUrl());
            serializer.endTag(null, "address");

            List<AccountDetails> myAccounts = site.getAccountList();
            serializer.startTag(null, "accounts");
            for (AccountDetails account : myAccounts) {
                serializer.startTag(null, "account");

                serializer.startTag(null, "username");
                serializer.text(account.getUserName());
                serializer.endTag(null, "username");

                serializer.startTag(null, "email");
                serializer.text(account.getEmail());
                serializer.endTag(null, "email");

                serializer.startTag(null, "password");
                serializer.text(account.getPassword());
                serializer.endTag(null, "password");

                serializer.startTag(null, "comments");
                serializer.text(account.getComments());
                serializer.endTag(null, "comments");

                serializer.endTag(null, "account");
            }
            serializer.endTag(null, "accounts");

            serializer.endTag(null, "site");
        }

        serializer.endTag(null, "sites");
        serializer.endDocument();
        serializer.flush();
        String data = writer.toString();
        return data;
    }

    public static List<Site> parseXml(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        List<Site> mySites = new ArrayList<>();
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(new InputSource(new StringReader(xmlString)));


        if (document != null) {

            NodeList nodeList = document.getElementsByTagName("site");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    String site = element.getElementsByTagName("address").item(0).getTextContent();
                    List<AccountDetails> myAccounts = new ArrayList<>();
                    NodeList myAccountsNodes = element.getElementsByTagName("account");
                    for (int j = 0; j < myAccountsNodes.getLength(); j++) {
                        Node accountNode = myAccountsNodes.item(j);
                        if (accountNode != null && accountNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element accountElement = (Element) accountNode;
                            String username = accountElement.getElementsByTagName("username").item(0).getTextContent();
                            String email = accountElement.getElementsByTagName("email").item(0).getTextContent();
                            String password = accountElement.getElementsByTagName("password").item(0).getTextContent();
                            String comments = accountElement.getElementsByTagName("comments").item(0).getTextContent();
                            AccountDetails accountDetails = new AccountDetails(username, email, password, comments);
                            myAccounts.add(accountDetails);
                        }
                    }
                    Site mySite = new Site(name, site, myAccounts);
                    mySites.add(mySite);
                }
            }

        }

        return mySites;
    }

}
