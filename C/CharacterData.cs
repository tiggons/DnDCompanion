using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CharacterSheet
{
    class PlayerLoot
    {
        public PlayerLoot()
        {
            powers = new List<string>();
        }
        public string item_name { get; set; }
        public string item_slot { get; set; }
        public string property { get; set; }
        public string ehancement { get; set; }
        public List<string> powers { get; set; }
    }

    class PlayerFeat
    {
        public PlayerFeat()
        {
        }

        public string name { get; set; }
        public string description { get; set; }
    }

    class PlayerStat
    {
        public PlayerStat()
        {
        }

        public ushort default_value { get; set; }
        public ushort current_value { get; set; }
    }

    class HealthInfo : PlayerStat
    {
        public ushort bloodied_value { get; set; }
        public ushort temporary_hitpoints { get; set; }
        public ushort healing_surges { get; set; }
    }

    class PowerWeapon
    {
        public PowerWeapon()
        {
            attack_bonus = 0;
            damage = "";
            crit_damage = "";
        }
        public ushort attack_bonus { get; set; }
        public string name { get; set; }
        public string damage { get; set; }
        public string crit_damage { get; set; }
    }

    class PlayerPower
    {
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
            weapons = new List<PowerWeapon>();
        }
        public enum power_usage_e
        {
            AT_WILL,
            ENCOUNTER,
            DAILY
        };
        public enum action_type_e
        {
            MINOR,
            MOVEMENT,
            STANDARD
        }
        public string name { get; set; }
        public string flavor { get; set; }
        public string display { get; set; }
        public power_usage_e power_usage { get; set; }
        public action_type_e action_type { get; set; }
        public string attack_type { get; set; }
        public string attack { get; set; }
        public string keywords { get; set; }
        public string hit { get; set; }
        public string miss { get; set; }
        public string effect { get; set; }
        public string target { get; set; }
        public List<PowerWeapon> weapons { get; set; }
    }

    class CharacterData
    {
        public CharacterData()
        {
            player_feats = new List<PlayerFeat>();
            class_features = new List<PlayerFeat>();
            racial_trait = new List<PlayerFeat>();
            player_loot = new List<PlayerLoot>();
            player_stats = new List<PlayerStat>();
            player_powers = new List<PlayerPower>();
            strength = new PlayerStat();
            dexterity = new PlayerStat();
            constitution = new PlayerStat();
            intelligence = new PlayerStat();
            wisdom = new PlayerStat();
            charisma = new PlayerStat();
            armor_class = new PlayerStat();
            fortitude = new PlayerStat();
            reflex = new PlayerStat();
            will = new PlayerStat();
            acrobatics = new PlayerStat();
            arcana = new PlayerStat();
            athletics = new PlayerStat();
            bluff = new PlayerStat();
            diplomacy = new PlayerStat();
            dungeoneering = new PlayerStat();
            endurance = new PlayerStat();
            heal = new PlayerStat();
            history = new PlayerStat();
            insight = new PlayerStat();
            intimidate = new PlayerStat();
            nature = new PlayerStat();
            perception = new PlayerStat();
            religion = new PlayerStat();
            stealth = new PlayerStat();
            streetwise = new PlayerStat();
            thievery = new PlayerStat();
            hit_points = new HealthInfo();
            player_stats.Add(strength);
            player_stats.Add(dexterity);
            player_stats.Add(constitution);
            player_stats.Add(intelligence);
            player_stats.Add(wisdom);
            player_stats.Add(charisma);
            player_stats.Add(armor_class);
            player_stats.Add(fortitude);
            player_stats.Add(reflex);
            player_stats.Add(will);
            player_stats.Add(acrobatics);
            player_stats.Add(arcana);
            player_stats.Add(athletics);
            player_stats.Add(bluff);
            player_stats.Add(diplomacy);
            player_stats.Add(dungeoneering);
            player_stats.Add(endurance);
            player_stats.Add(heal);
            player_stats.Add(history);
            player_stats.Add(insight);
            player_stats.Add(intimidate);
            player_stats.Add(nature);
            player_stats.Add(perception);
            player_stats.Add(religion);
            player_stats.Add(stealth);
            player_stats.Add(streetwise);
            player_stats.Add(thievery);
            player_stats.Add(hit_points);
        }

        public void reset_all_values_to_default()
        {
            foreach (PlayerStat stat in player_stats)
            {
                stat.current_value = stat.default_value;
            }
        }

        private List<PlayerStat> player_stats;
        public List<PlayerFeat> player_feats { get; set; }
        public List<PlayerFeat> class_features { get; set; }
        public List<PlayerFeat> racial_trait { get; set; }
        public List<PlayerLoot> player_loot { get; set; }
        public List<PlayerPower> player_powers { get; set; }
        public PlayerStat strength { get; set; }
        public PlayerStat dexterity { get; set; }
        public PlayerStat constitution { get; set; }
        public PlayerStat intelligence { get; set; }
        public PlayerStat wisdom { get; set; }
        public PlayerStat charisma { get; set; }
        public PlayerStat armor_class { get; set; }
        public PlayerStat fortitude { get; set; }
        public PlayerStat reflex { get; set; }
        public PlayerStat will { get; set; }
        public PlayerStat acrobatics { get; set; }
        public PlayerStat arcana { get; set; }
        public PlayerStat athletics { get; set; }
        public PlayerStat bluff { get; set; }
        public PlayerStat diplomacy { get; set; }
        public PlayerStat dungeoneering { get; set; }
        public PlayerStat endurance { get; set; }
        public PlayerStat heal { get; set; }
        public PlayerStat history { get; set; }
        public PlayerStat insight { get; set; }
        public PlayerStat intimidate { get; set; }
        public PlayerStat nature { get; set; }
        public PlayerStat perception { get; set; }
        public PlayerStat religion { get; set; }
        public PlayerStat stealth { get; set; }
        public PlayerStat streetwise { get; set; }
        public PlayerStat thievery { get; set; }
        public HealthInfo hit_points { get; set; }
    }
}
