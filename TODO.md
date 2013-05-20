## TODO ##

### Known Bugs ###

Bounty System:
 * When quests are removed, the bolded description does not update properly
 * Bounties should really update using a cron job (see cron.xml), so that they complete when they say they will, instead of the next time a user logs on

Battles:
 * Sometimes at the end of a battle, some dragons do not show experience gain. It is not clear if they are simply not gaining experience, or if it is just not showing it with a post-BattleAction

Quests:
 * The CreateQuestPage is not currently creating quests... this requires some investigation. Currently all quests are created manually

### Future Work ###

Battles:
 * Make the attack animation show what skill is being used (could incorporate the skill's icon)
 * Allow the user to hover of dragons and buffs to get more details, also how they buff affects the dragon. If it's 15% extra str, show how much that comes to.
 * We could always use more balancing and testing
 * An automated battle schedule/pairing system
 * Teams/guilds for team battles, that are reworked perhaps every 2 weeks to keep things balanced
 * Win/Loss tracking
 * Possibly make it where users have more control over a battle. Have it where they can choose which attacks they want their dragon to perform.

Skills:
 * Consider a system where users can "train" their dragons to use certain skill in certain situations, rather than it being completely random
 * Consider giving skills a "frequency" property that would weight the chance of it being picked (so buffing skills are less likely than attach skills)
 * Have ActiveSkills define a chance of inflicting a Buff, rather than having it always happen
 * Consider having more than one variety of skill points. Instead of all points coming from quests, perhaps one type could come from good grades on assignments, another from helping classmates. Skills could cost a certain combination of those different skill points, encouraging students to do a larger variety of enhancing activities.
 
Buffs:
 * Give buffs a property that determines how long they last

Users:
 * Create more DragonClasses for user to choose from. Keep them balanced with the methods in BattlePage
 * Consider giving the user more control over how stats are allocated, instead of having it determined entirely by the DragonClass

Items:
 * They currently exist in the game, and can be added to a user, but they have no function. The plan is to have items have a Skill attached to them, either passively or as a one-time use.
 * Players should be able to equip items to a battle and have the dragon use/benefit from them during the battle.
 * Some items should be consumed upon use, and other should be permanent
 * There should be a limit on the number of items from each category that can be equipped for a given battle
 * Possibly have it where teachers have a page to create custom items if they want. These items don't have to have an impact on gameplay.
 * Some items are hidden "secret items" that don't show up on quest details page as an obtainable item
 * Item artwork

Bounties:
 * Implement bounty completion as a cron job on the server. The current implementation is flawed and causes bugs.
 * Balance bounties to have a larger impact of player progression
 * Consider creating a system to generate or allow the creation of bounties over time to give player more incentive to see what's new and add immersion.
 * Consider creating a tutorial of sorts to introduce players to the bounty system
 * Consider adding battles (with experience) against NPCs at the end of some bounties to add some challenge/levels of difficulty.
 
Quests:
 * Code is there just needs to be implemented: Have it where students unlock some quests by completing certain ones prior to that one.
 * Make certain quests available at a certain time and date and unavailable at a certain time and date. (Code is there for the most part)
 * Make some quests that are completed based on student's honor. (Code is there for the most part)
 * Make some quests that are completed based on teacher confirmation. (Code is there for the most part)
 * Have a page where teacher can edit,update, and delete quests. (Partially coded, need rpc calls put in)
 * Icon on home page representing the type of quest available i.e scroll img icon for code quests
 
Newsfeed:
 * Possible integration of player icons (using gravatar.com) into the newsfeed
 * More newsfeed items
