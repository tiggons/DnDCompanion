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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class XMLCharacterSheetReader extends DefaultHandler
{
    private CharacterData charData;
    //private Document char_doc = null;
    private String filename;
    private PlayerStat tmpStat;
    private PlayerLoot tmpLoot;
    private PlayerPower tmpPower;
    private PowerWeapon tmpPowerWeapon;
    private PlayerFeat  tmpPlayerFeat;
    private String tmpSpecificPowerName;
    private String tmpTagText;
    private String tmpRuleType;
    private boolean ruleParseDescription = false;
    public enum ParseModeE
    {
    	TOPLEVEL,
    	STATBLOCK,
    	LOOTBLOCK,
    	POWERBLOCK,
    	RULESELEMENTBLOCK
    }
    private ParseModeE parseMode = ParseModeE.TOPLEVEL;
    private boolean lootEquipped = false;
    private boolean lootShowPowerCard = false;

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
			sp.parse(new InputSource(new FileInputStream(filename)), this);
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
    	switch (parseMode)
    	{
    		case TOPLEVEL:
    			if(qName.equalsIgnoreCase("StatBlock")) 
    	        {
    				parseMode = ParseModeE.STATBLOCK;
    			}else if(qName.equalsIgnoreCase("LootTally"))
    			{
    				parseMode = ParseModeE.LOOTBLOCK;
    			}else if(qName.equalsIgnoreCase("PowerStats"))
    			{
    				parseMode = ParseModeE.POWERBLOCK;
    			}else if(qName.equalsIgnoreCase("RulesElement"))
    			{
    				parseMode = ParseModeE.RULESELEMENTBLOCK;
    			}
    			break;
    		case STATBLOCK:
    			startStatBlock(uri,localName,qName,attributes);
    			break;
    		case LOOTBLOCK:
    			startLootBlock(uri,localName,qName,attributes);
    			break;
    		case POWERBLOCK:
    			startPowerBlock(uri,localName,qName,attributes);
    			break;
    		case RULESELEMENTBLOCK:
    			startRulesElementBlock(uri,localName,qName,attributes);
    			break;
    		default:
    			break;
    	}		
	}

    private void startStatBlock(String uri, String localName, String qName,
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
			//Do nothing with statadd for now
        }
    }
    
    private void startLootBlock(String uri, String localName, String qName,
            Attributes attributes) throws SAXException
    {
    	if(qName.equalsIgnoreCase("loot")) 
        {
    		tmpLoot = new PlayerLoot();
		}else if(qName.equalsIgnoreCase("equip-count"))
		{
			lootEquipped = true;
		}else if(qName.equalsIgnoreCase("ShowPowerCard"))
		{
			lootShowPowerCard = true;
		}else if(qName.equalsIgnoreCase("RulesElement"))
		{
			if (lootEquipped == true && lootShowPowerCard == true)
			{
				tmpLoot.itemName = attributes.getValue("name");
			}
		}else if(qName.equalsIgnoreCase("specific"))
		{
			if (lootEquipped == true && lootShowPowerCard == true)
			{
				String specificName = attributes.getValue("name");
				if(specificName.toLowerCase() == "item slot")
				{
                    tmpLoot.itemSlot = specificName;
				}else if(specificName.toLowerCase() == "enhancement")
				{
                    tmpLoot.enhancement = specificName;
				}else if(specificName.toLowerCase() == "property")
				{
                    tmpLoot.property = specificName;
				}else if(specificName.toLowerCase() == "power")
				{
                    tmpLoot.powers.add(specificName);
				}
			}
		}
    }
    
    private void startPowerBlock(String uri, String localName, String qName,
            Attributes attributes) throws SAXException
    {
    	if(qName.equalsIgnoreCase("Power")) 
        {
    		tmpPower = new PlayerPower();
		}else if(qName.equalsIgnoreCase("specific"))
		{
			tmpSpecificPowerName = attributes.getValue("name");
		}
		else if(qName.equalsIgnoreCase("Weapon"))
		{
			if (!attributes.getValue("name").equalsIgnoreCase("Unarmed"))
			{
				tmpPowerWeapon = new PowerWeapon();
				tmpPowerWeapon.name = attributes.getValue("name");
			}
		}
    }
    
    private void parseSpecificPower()
    {
    	if(tmpSpecificPowerName.equalsIgnoreCase("Flavor"))
		{
			tmpPower.flavor = tmpTagText;
			tmpTagText = "";
			tmpSpecificPowerName = "";
		}else if(tmpSpecificPowerName.equalsIgnoreCase("Display"))
		{
			tmpPower.display = tmpTagText;
			tmpTagText = "";
			tmpSpecificPowerName = "";
		}else if(tmpSpecificPowerName.equalsIgnoreCase("Keywords"))
		{
			tmpPower.keywords = tmpTagText;
			tmpTagText = "";
			tmpSpecificPowerName = "";
		}else if(tmpSpecificPowerName.equalsIgnoreCase("Attack Type"))
		{
			tmpPower.attackType = tmpTagText;
			tmpTagText = "";
			tmpSpecificPowerName = "";
		}else if(tmpSpecificPowerName.equalsIgnoreCase("Target"))
		{
			tmpPower.target = tmpTagText;
			tmpTagText = "";
			tmpSpecificPowerName = "";
		}else if(tmpSpecificPowerName.equalsIgnoreCase("Attack"))
		{
			tmpPower.attack = tmpTagText;
			tmpTagText = "";
			tmpSpecificPowerName = "";
		}else if(tmpSpecificPowerName.equalsIgnoreCase("Hit"))
		{
			tmpPower.hit = tmpTagText;
			tmpTagText = "";
			tmpSpecificPowerName = "";
		}else if(tmpSpecificPowerName.equalsIgnoreCase("Miss"))
		{
			tmpPower.miss = tmpTagText;
			tmpTagText = "";
			tmpSpecificPowerName = "";
		}else if(tmpSpecificPowerName.equalsIgnoreCase("Effect"))
		{
			tmpPower.effect = tmpTagText;
			tmpTagText = "";
			tmpSpecificPowerName = "";
		}else if(tmpSpecificPowerName.equalsIgnoreCase("Power Usage"))
		{
			if (tmpTagText.equalsIgnoreCase("At-Will"))
			{
				tmpPower.powerUsage = PlayerPower.PowerUsageE.AT_WILL;
			}else if (tmpTagText.equalsIgnoreCase("Encounter"))
			{
				tmpPower.powerUsage = PlayerPower.PowerUsageE.ENCOUNTER;
			}else if (tmpTagText.equalsIgnoreCase("Daily"))
			{
				tmpPower.powerUsage = PlayerPower.PowerUsageE.DAILY;
			}
			tmpTagText = "";
		}else if(tmpSpecificPowerName.equalsIgnoreCase("Action Type"))
		{
			if (tmpTagText.equalsIgnoreCase("Minor Action"))
			{
				tmpPower.actionType = PlayerPower.ActionTypeE.MINOR;
			}else if (tmpTagText.equalsIgnoreCase("Move Action"))
			{
				tmpPower.actionType = PlayerPower.ActionTypeE.MOVEMENT;
			}else if (tmpTagText.equalsIgnoreCase("Standard Action"))
			{
				tmpPower.actionType = PlayerPower.ActionTypeE.STANDARD;
			}
			tmpTagText = "";
		}
    }
    
    private void startRulesElementBlock(String uri, String localName, String qName,
            Attributes attributes) throws SAXException
    {
    	if(qName.equalsIgnoreCase("RulesElement")) 
        {
    		tmpRuleType = attributes.getValue("type");
    		tmpPlayerFeat = new PlayerFeat(attributes.getValue("name"));
		}if(qName.equalsIgnoreCase("specific"))
		{
			if (attributes.getValue("name").equalsIgnoreCase("Short Description"))
			{
				ruleParseDescription = true;
			}
		}
    }

    public void endElement(String uri, String localName, String qName) 
        throws SAXException 
    {
    	switch (parseMode)
    	{
    		case STATBLOCK:
    			if(qName.equalsIgnoreCase("StatBlock")) 
    	        {
    				parseMode = ParseModeE.TOPLEVEL;
    			}else
    			{
    				endStatBlock(uri,localName,qName);
    			}
    			break;
    		case LOOTBLOCK:
    			if(qName.equalsIgnoreCase("LootTally")) 
    	        {
    				parseMode = ParseModeE.TOPLEVEL;
    				lootEquipped = false;
    				lootShowPowerCard = false;
    			}else
    			{
    				endLootBlock(uri,localName,qName);
    			}
    			break;
    		case POWERBLOCK:
    			if(qName.equalsIgnoreCase("PowerStats")) 
    	        {
    				parseMode = ParseModeE.TOPLEVEL;
    			}else
    			{
    				endPowerBlock(uri,localName,qName);
    			}
    			break;
    		case RULESELEMENTBLOCK:
    			if(qName.equalsIgnoreCase("RulesElementTally")) 
    	        {
    				parseMode = ParseModeE.TOPLEVEL;
    			}else
    			{
    				endRulesElementBlock(uri,localName,qName);
    			}
    			break;
    		default:
    			break;
    	}	
		
	}
    
    private void endStatBlock(String uri, String localName, String qName) 
            throws SAXException 
    {
    	if(qName.equalsIgnoreCase("Stat")) 
        {
			//add it to the list
            if(tmpStat != null && tmpStat.name != null)
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
    
    private void endLootBlock(String uri, String localName, String qName) 
            throws SAXException 
    {
    	if (qName.equalsIgnoreCase("loot"))
    	{
    		if (tmpLoot != null)
    		{
    			charData.playerLoot.add(tmpLoot);
    			tmpLoot = null;
    		}
    	}
    }
    
    private void endPowerBlock(String uri, String localName, String qName) 
            throws SAXException 
    {
    	if(qName.equalsIgnoreCase("Power")) 
        {
    		if (tmpPower != null)
    		{
    			charData.playerPowers.add(tmpPower);
    			tmpPower = null;
    		}
		}else if(qName.equalsIgnoreCase("specific"))
		{
			parseSpecificPower();
		}else if(qName.equalsIgnoreCase("Weapon"))
		{
			if (tmpPowerWeapon != null && tmpPower != null)
			{
				tmpPower.weapons.add(tmpPowerWeapon);
			}
		}else if(qName.equalsIgnoreCase("AttackBonus"))
		{
			if (tmpPowerWeapon != null && tmpPower != null)
			{
				try
				{
					tmpPowerWeapon.attackBonus = Integer.parseInt(tmpTagText.trim());
				}catch(Exception e)
				{
					Log.d("INT", tmpTagText + " " + e.getLocalizedMessage());
				}
			}
			tmpTagText = "";
		}else if(qName.equalsIgnoreCase("CritDamage"))
		{
			if (tmpPowerWeapon != null && tmpPower != null)
			{
				tmpPowerWeapon.critDamage = tmpTagText;
			}
			tmpTagText = "";
		}else if(qName.equalsIgnoreCase("Damage"))
		{
			if (tmpPowerWeapon != null && tmpPower != null)
			{
				tmpPowerWeapon.damage = tmpTagText;
			}
			tmpTagText = "";
		}
    }
    
    private void endRulesElementBlock(String uri, String localName, String qName) 
            throws SAXException 
    {
    	if (qName.equalsIgnoreCase("specific"))
    	{
    		if (ruleParseDescription)
    		{
    			if (tmpRuleType.equalsIgnoreCase("Feat"))
    			{
    				tmpPlayerFeat.description = tmpTagText;
    				charData.playerFeats.add(tmpPlayerFeat);
    				tmpTagText = "";
    				tmpRuleType = "";
    				tmpPlayerFeat = null;
    			}else if (tmpRuleType.equalsIgnoreCase("Class Feature"))
    			{
    				tmpPlayerFeat.description = tmpTagText;
    				charData.classFeatures.add(tmpPlayerFeat);
    				tmpTagText = "";
    				tmpRuleType = "";
    				tmpPlayerFeat = null;
    			}else if (tmpRuleType.equalsIgnoreCase("Racial Trait"))
    			{
    				tmpPlayerFeat.description = tmpTagText;
    				charData.racialTrait.add(tmpPlayerFeat);
    				tmpTagText = "";
    				tmpRuleType = "";
    				tmpPlayerFeat = null;
    			}
    		}
    	}	
    }

    public void characters(char ch[], int start, int length) throws SAXException 
    {
    	tmpTagText = new String(ch,start,length);
    	tmpTagText = tmpTagText.trim();
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
