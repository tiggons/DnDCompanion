package dndcompanion;
    
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.xml.transform.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.dom4j.*;
import org.dom4j.io.*;

public class XMLCharacterSheetReader extends DefaultHandler
{
    private CharacterData charData;
    //private Document char_doc = null;
    private String filename;
    private PlayerStat tmpStat;

    public XMLCharacterSheetReader(String filename)
    {
        this.filename = filename;
        charData = new CharacterData();
    }

    public CharacterData loadCharacterSheet()
    {
        parseXmlFile();
        return charData;
    }

    public void test()
    {
        parseXmlFile();
        printData();
    }

    private void parseXmlFile(){
        //get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();

		try 
        {
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			//parse the file and also register this class for call backs
			sp.parse(filename, this);
		}
        catch(SAXException se) 
        {
			se.printStackTrace();
		}
        catch(ParserConfigurationException pce) 
        {
			pce.printStackTrace();
		}
        catch (IOException ie) 
        {
			ie.printStackTrace();
		}
    }

    public void startElement(String uri, String localName, String qName, 
                             Attributes attributes) throws SAXException 
    {
		if(qName.equalsIgnoreCase("Stat")) 
        {
			//System.out.println("attr value "+attributes.getValue("value"));
            tmpStat = new PlayerStat(new Integer(attributes.getValue("value")),
                                     new Integer(attributes.getValue("value")));
		}
        else if (qName.equalsIgnoreCase("alias")) 
        {
            String stat_name = attributes.getValue("name");
            if(charData.KEY_STATS.contains(stat_name)) 
            {
                tmpStat.name = stat_name;
            } 
        }
        else if (qName.equalsIgnoreCase("statadd")) 
        {
			//tmpStat.currentValue =+ new Integer(attributes.getValue("value"));
        }
	}



    public void endElement(String uri, String localName, String qName) 
        throws SAXException 
    {
		if(qName.equalsIgnoreCase("Stat")) 
        {
			//add it to the list
            if(tmpStat.name != null)
            {
                if(tmpStat.name.equals("Hit Points")) 
                {
                    HealthInfo hpStat = new HealthInfo(tmpStat.defaultValue,
                                                       tmpStat.currentValue,
                                                       tmpStat.defaultValue/2,
                                                       0,0);
                    charData.hitPoints = hpStat;
                } 
                else 
                {
                    charData.playerStats.put(tmpStat.name, tmpStat);
                }
            }
		}
	}

    
    /**
	 * Iterate through the list and print
	 * the contents
	 */
	private void printData()
    {
		System.out.println("CharacterData: \n");
        charData.print();
	}

	public static void main(String[] args)
    {
		XMLCharacterSheetReader csReader = new XMLCharacterSheetReader(args[0]);
		csReader.test();
	}
}
