package dndcompanion;

import java.util.*;
import java.io.*;

import org.dom4j.*;
import org.dom4j.io.*;


/**
 * DnDCompanion Character Data classes
 *
 * @author translated from C by Michelle Redick
 * @author original C code by AJ Lindell
 * @version $Revision: 1.0 $ $Date:$
 */
class PlayerLoot
{
    public String itemName;
    public String itemSlot;
    public String property;
    public String enhancement;
    public List powers;

    public PlayerLoot()
    {
        powers = new ArrayList();
    }

    public PlayerLoot(String name, String slot, String property, 
                      String enhancement, List powers)
    {
        itemName = name;
        itemSlot = slot;
        this.property = property;
        this.powers = powers;
    }
}

class PlayerFeat
{
    public String name;
    public String description;
    
    public PlayerFeat(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

}

class PlayerStat
{
    public Integer defaultValue;
    public Integer currentValue;
    public String name;
    public String abrv;

    public PlayerStat()
    {
        name = null;
        defaultValue = 0;
        currentValue = 0;
    }

    public PlayerStat(Integer defaultValue, Integer currentValue)
    {
        name = null;
        this.defaultValue = defaultValue;
        this.currentValue = currentValue;
    }
}

class HealthInfo extends PlayerStat
{
    public Integer bloodiedValue;
    public Integer temporaryHitPoints;
    public Integer healingSurges;

    public HealthInfo()
    {
        name = "Hit Points";
        abrv = "HP";
        bloodiedValue = 0;
        temporaryHitPoints = 0;
        healingSurges = 0;
    }

    public HealthInfo(Integer defaultValue, Integer currentValue,
                      Integer bloodiedValue, Integer tempHitPoints,
                      Integer healingSurges)
    {
        super(defaultValue, currentValue);

        name = "Hit Points";
        abrv = "HP";
        this.bloodiedValue = bloodiedValue;
        this.temporaryHitPoints = tempHitPoints;
        this.healingSurges = healingSurges;
    }
}

class PowerWeapon
{
    public Integer attackBonus;
    public String name;
    public String damage;
    public String critDamage;

    public PowerWeapon()
    {
        this.attackBonus = 0;
        this.damage = "";
        this.critDamage = "";
    }
    public PowerWeapon(String Name)
    {
        this.name = name;
        this.attackBonus = 0;
        this.damage = "";
        this.critDamage = "";
    }
    public PowerWeapon(Integer attackBonus, String Name, 
                       String damage, String critDamage)
    {
        this.name = name;
        this.attackBonus = 0;
        this.damage = "";
        this.critDamage = "";
    }
}

class PlayerPower
{
    public enum PowerUsageE { AT_WILL, ENCOUNTER, DAILY }
    public enum ActionTypeE { MINOR, MOVEMENT, STANDARD }

    public String name;
    public String flavor;
    public String display;
    public PowerUsageE powerUsage;
    public ActionTypeE actionType;
    public String attackType;
    public String attack;
    public String keywords;
    public String hit;
    public String miss;
    public String effect;
    public String target;
    //public String special;
    public List<PowerWeapon> weapons;

    public PlayerPower()
    {
        name = "";
        flavor = "";
        attack = "";
        keywords = "";
        hit = "";
        miss = "";
        effect = "";
        target = "";
        weapons = new ArrayList<PowerWeapon>();
    }
}

public class CharacterData
{
    public HashMap<String,PlayerStat> playerStats;
    public List<String> KEY_STATS;
    public List<PlayerFeat> playerFeats;
    public List<PlayerFeat> classFeatures;
    public List<PlayerFeat> racialTrait;
    public List<PlayerLoot> playerLoot;
    public List<PlayerPower> playerPowers;
    public HealthInfo hitPoints;


    public CharacterData()
    {
        playerFeats = new ArrayList<PlayerFeat>();
        classFeatures = new ArrayList<PlayerFeat>();
        racialTrait = new ArrayList<PlayerFeat>();
        playerLoot = new ArrayList<PlayerLoot>();
        playerStats = new HashMap<String,PlayerStat>();
        playerPowers = new ArrayList<PlayerPower>();
        hitPoints = new HealthInfo();
        KEY_STATS = new ArrayList<String>();
        KEY_STATS.add("Charisma");
        KEY_STATS.add("Dexterity");
        KEY_STATS.add("Intelligence");
        KEY_STATS.add("Wisdom");
        KEY_STATS.add("Constitution");
        KEY_STATS.add("Strength");
        KEY_STATS.add("Armor Class");
        KEY_STATS.add("Fortitude");
        KEY_STATS.add("Reflex");
        KEY_STATS.add("Will");
        KEY_STATS.add("Healing Surges");
        KEY_STATS.add("Acrobatics");
        KEY_STATS.add("Arcana");
        KEY_STATS.add("Athletics");
        KEY_STATS.add("Bluff");
        KEY_STATS.add("Diplomacy");
        KEY_STATS.add("Dungeoneering");
        KEY_STATS.add("Endurance");
        KEY_STATS.add("Heal");
        KEY_STATS.add("History");
        KEY_STATS.add("Insight");
        KEY_STATS.add("Intimidate");
        KEY_STATS.add("Nature");
        KEY_STATS.add("Perception");
        KEY_STATS.add("Religion");
        KEY_STATS.add("Stealth");
        KEY_STATS.add("Streetwise");
        KEY_STATS.add("Thievery");
        KEY_STATS.add("Hit Points");

    }

    public void resetAllValuesToDefault()
    {
        PlayerStat stat;
        Set keys = playerStats.keySet();

        for(Iterator<String> i = keys.iterator(); i.hasNext();)
        {
            stat = playerStats.get(i.next());
            stat.currentValue = stat.defaultValue;
        }
    }

    public void print()
    {
        PlayerStat stat;
        Set keys = playerStats.keySet();

        for(Iterator<String> i = keys.iterator(); i.hasNext();)
        {
            stat = playerStats.get(i.next());
            System.out.println("playerStat "+stat.name+"("
                               +stat.abrv+") current="
                               +stat.currentValue+" default="
                               +stat.defaultValue);
        }
    }

}
