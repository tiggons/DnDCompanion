using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;
using System.Xml.Linq;
using System.IO;
using Windows.Storage;
using Windows.Storage.Streams;

namespace CharacterSheet
{
    class XMLCharacterSheetReader
    {
        private XmlReader xml_doc;
        private CharacterData charData;

        public XMLCharacterSheetReader()
        {
            charData = new CharacterData();
        }
        public CharacterData loadCharacterSheet()
        {
            parse_xml_file();
            return charData;
        }

        private void parse_xml_file()
        {
            //OpenFileDialog file_in = new OpenFileDialog();
            //if (file_in.ShowDialog() == DialogResult.OK)
            //{
            //StreamReader sr = new StreamReader(file_in.FileName);
            XDocument x_doc = XDocument.Load("C:\\Users\\Adge\\Desktop\\FLARK.dnd4e");
            XElement stat_block = x_doc.Descendants("StatBlock").First();
            parse_stat_block(stat_block);
            XElement loot_block = x_doc.Descendants("LootTally").First();
            parse_loot_block(loot_block);
            XElement power_block = x_doc.Descendants("PowerStats").First();
            parse_power_block(power_block);
            XElement rules_element_tally = x_doc.Descendants("RulesElementTally").First();
            IEnumerable<XElement> rules_element_block = rules_element_tally.Descendants("RulesElement");
            parse_rules_element_block(rules_element_block);
            //}
        }

        private void parse_rules_element_block(IEnumerable<XElement> element_block)
        {
            foreach (XElement feat in element_block)
            {
                IEnumerable<XAttribute> feat_attributes = feat.Attributes();
                string feat_name = "", feat_type = "";
                foreach (XAttribute feat_attribute in feat_attributes)
                {
                    if (feat_attribute.Name.ToString() == "type")
                    {
                        feat_type = feat_attribute.Value.ToString();
                    }
                    if (feat_attribute.Name.ToString() == "name")
                    {
                        feat_name = feat_attribute.Value.ToString();
                    }
                }
                /* Check if it is a feat */
                switch (feat_type)
                {
                    case "Feat":
                        parse_feat(feat, feat_name);
                        break;
                    case "Class Feature":
                        parse_class_feature(feat, feat_name);
                        break;
                    case "Racial Trait":
                        parse_racial_trait(feat, feat_name);
                        break;
                    default:
                        break;
                }
            }
        }

        private void parse_racial_trait(XElement racial_trait, string trait_name)
        {
            PlayerFeat new_feat = new PlayerFeat();
            IEnumerable<XElement> specifics = racial_trait.Descendants("specific");
            new_feat.name = trait_name;
            foreach (XElement specific in specifics)
            {
                specific.GetType();
                if (specific.FirstAttribute.Value.ToString() == "Short Description")
                {
                    new_feat.description = specific.Value;
                }
            }
            charData.racial_trait.Add(new_feat);
        }

        private void parse_class_feature(XElement class_feature, string feature_name)
        {
            PlayerFeat new_feat = new PlayerFeat();
            IEnumerable<XElement> specifics = class_feature.Descendants("specific");
            new_feat.name = feature_name;
            foreach (XElement specific in specifics)
            {
                if (specific.FirstAttribute.Value.ToString() == "Short Description")
                {
                    new_feat.description = specific.Value;
                }
            }
            charData.class_features.Add(new_feat);
        }

        private void parse_feat(XElement feat, string feat_name)
        {
            PlayerFeat new_feat = new PlayerFeat();
            IEnumerable<XElement> specifics = feat.Descendants("specific");
            new_feat.name = feat_name;
            foreach (XElement specific in specifics)
            {
                if (specific.FirstAttribute.Value.ToString() == "Short Description")
                {
                    new_feat.description = specific.Value;
                }
            }
            charData.player_feats.Add(new_feat);
        }

        private void parse_loot_block(XElement xml_doc)
        {
            IEnumerable<XElement> loot_list = xml_doc.Descendants("loot");
            bool is_equipped, show_power;
            foreach (XElement loot in loot_list)
            {
                IEnumerable<XAttribute> loot_attributes = loot.Attributes();
                is_equipped = false;
                show_power = false;
                foreach (XAttribute loot_attribute in loot_attributes)
                {
                    /* Check if loot is equipped */
                    if (loot_attribute.Name.ToString() == "equip-count")
                    {
                        if (Convert.ToInt16(loot_attribute.Value) > 0)
                        {
                            is_equipped = true;
                        }
                    }
                    if (loot_attribute.Name.ToString() == "ShowPowerCard")
                    {
                        if (Convert.ToInt16(loot_attribute.Value) > 0)
                        {
                            show_power = true;
                        }
                    }
                }
                if (is_equipped && show_power)
                {
                    parse_loot_section(loot);
                }
            }
        }

        private void parse_loot_section(XElement rules_element)
        {
            PlayerLoot player_loot = new PlayerLoot();

            IEnumerable<XElement> elements = rules_element.Descendants("RulesElement");
            player_loot.item_name = rules_element.Descendants("RulesElement").Last().FirstAttribute.Value.ToString();
            foreach (XElement element in elements)
            {
                IEnumerable<XElement> loot_info = element.Descendants("specific");
                foreach (XElement info in loot_info)
                {
                    XAttribute attribute = info.FirstAttribute;
                    string name = attribute.Value.ToString();
                    switch (name)
                    {
                        case "Item Slot":
                            player_loot.item_slot = info.Value.ToString();
                            break;
                        case "Enhancement":
                            player_loot.ehancement = info.Value.ToString();
                            break;
                        case "Property":
                            player_loot.property = info.Value.ToString();
                            break;
                        case "Power":
                            player_loot.powers.Add(info.Value.ToString());
                            break;
                        default:
                            break;
                    }
                }
            }
            charData.player_loot.Add(player_loot);
        }

        private void parse_power_block(XElement xml_doc)
        {
            IEnumerable<XElement> power_list = xml_doc.Descendants("Power");
            foreach (XElement power in power_list)
            {
                PlayerPower new_power = new PlayerPower();
                XAttribute power_name = power.FirstAttribute;
                IEnumerable<XElement> power_specifics = power.Descendants("specific");
                IEnumerable<XElement> power_weapons = power.Descendants("Weapon");
                new_power.name = power_name.Value;
                foreach (XElement specific in power_specifics)
                {
                    XAttribute power_info = specific.FirstAttribute;
                    switch (power_info.Value)
                    {
                        case "Flavor":
                            new_power.flavor = specific.Value;
                            break;
                        case "Power Usage":
                            switch (specific.Value)
                            {
                                case "At-Will":
                                    new_power.power_usage = PlayerPower.power_usage_e.AT_WILL;
                                    break;
                                case "Encounter":
                                    new_power.power_usage = PlayerPower.power_usage_e.ENCOUNTER;
                                    break;
                                case "Daily":
                                    new_power.power_usage = PlayerPower.power_usage_e.DAILY;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case "Display":
                            new_power.display = specific.Value;
                            break;
                        case "Keywords":
                            new_power.keywords = specific.Value;
                            break;
                        case "Attack Type":
                            new_power.attack_type = specific.Value;
                            break;
                        case "Target":
                            new_power.target = specific.Value;
                            break;
                        case "Attack":
                            new_power.attack = specific.Value;
                            break;
                        case "Hit":
                            new_power.hit = specific.Value;
                            break;
                        case "Miss":
                            new_power.miss = specific.Value;
                            break;
                        case "Action Type":
                            switch (specific.Value)
                            {
                                case "Standard Action":
                                    new_power.action_type = PlayerPower.action_type_e.STANDARD;
                                    break;
                                case "Minor Action":
                                    new_power.action_type = PlayerPower.action_type_e.MINOR;
                                    break;
                                case "Move Action":
                                    new_power.action_type = PlayerPower.action_type_e.MOVEMENT;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case "Effect":
                            new_power.effect = specific.Value;
                            break;
                        default:
                            break;
                    }
                    power_info.GetType();
                    specific.GetType();
                }
                foreach (XElement weapon in power_weapons)
                {
                    PowerWeapon new_weapon = new PowerWeapon();
                    if (weapon.FirstAttribute.Value == "Unarmed")
                    {
                        continue;
                    }
                    new_weapon.name = weapon.FirstAttribute.Value;
                    new_weapon.attack_bonus = Convert.ToByte(weapon.Descendants("AttackBonus").First().Value);
                    new_weapon.crit_damage = weapon.Descendants("CritDamage").First().Value;
                    new_weapon.damage = weapon.Descendants("Damage").First().Value;
                    new_power.weapons.Add(new_weapon);
                }
                charData.player_powers.Add(new_power);
            }
        }

        private void parse_stat_block(XElement xml_doc)
        {
            string stat_value = "0";
            string stat_name;
            IEnumerable<XElement> stat_list = xml_doc.Descendants("Stat");
            foreach (XElement stat in stat_list)
            {
                XElement stat_alias = stat.Descendants("alias").First();
                XAttribute value_attribute = stat.FirstAttribute;
                XAttribute name_attribute = stat_alias.FirstAttribute;
                stat_value = value_attribute.Value;
                stat_name = name_attribute.Value;
                switch (stat_name)
                {
                    case "CHA":
                    case "cha":
                    case "Charisma":
                        charData.charisma.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "DEX":
                    case "dex":
                    case "Dexterity":
                        charData.dexterity.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "INT":
                    case "int":
                    case "Intelligence":
                        charData.intelligence.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "WIS":
                    case "wis":
                    case "Wisdom":
                        charData.wisdom.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "CON":
                    case "con":
                    case "Constitution":
                        charData.constitution.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "STR":
                    case "str":
                    case "Strength":
                        charData.strength.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "AC":
                    case "Armor Class":
                        charData.armor_class.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Fortitude":
                    case "Fortitude Defense":
                        charData.fortitude.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Reflex":
                    case "Reflex Defense":
                        charData.reflex.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Will":
                    case "Will Defense":
                        charData.will.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Hit Points":
                        charData.hit_points.default_value = Convert.ToUInt16(stat_value);
                        charData.hit_points.bloodied_value = (ushort)(charData.hit_points.default_value / 2);
                        break;
                    case "Healing Surges":
                        charData.hit_points.healing_surges = Convert.ToUInt16(stat_value);
                        break;
                    case "Acrobatics":
                        charData.acrobatics.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Arcana":
                        charData.arcana.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Athletics":
                        charData.athletics.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Bluff":
                        charData.bluff.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Diplomacy":
                        charData.diplomacy.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Dungeoneering":
                        charData.dungeoneering.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Endurance":
                        charData.endurance.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Heal":
                        charData.heal.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "History":
                        charData.history.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Insight":
                        charData.insight.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Intimidate":
                        charData.intimidate.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Nature":
                        charData.nature.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Perception":
                        charData.perception.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Religion":
                        charData.religion.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Stealth":
                        charData.stealth.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Streetwise":
                        charData.streetwise.default_value = Convert.ToUInt16(stat_value);
                        break;
                    case "Thievery":
                        charData.thievery.default_value = Convert.ToUInt16(stat_value);
                        break;
                    default:
                        break;
                }
            }
            charData.reset_all_values_to_default();
        }
    }
}
