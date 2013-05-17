# Clashroom #

## About Clashroom ##
Clashroom is online, multiplayer RPG where players take on the role of Dragon trainers. Players take on real-world quests to give their Dragons experience, items and new skills. When played in the context of a classroom setting, the real-world quests can be things like getting help from a tutor, doing practice problems or doing supplementary research. Dragons face off against each other in the arena, so these quests become vital to success in the game. The hope is that adding a Clashroom layer to a class can encourage students to get involved in the subject outside of class in fun, voluntary way.

## Prerequisites ##
Clashroom is coded using Google Web Toolkit with a JDO/App Engine backend. Knowledge of each of these technologies is necessary to contribute to Clashroom. See https://developers.google.com/web-toolkit/ for a starting guide to GWT.

An understanding of git is also important for contributing. While a single contributor will be successful using the GitHub client, if multiple contributors are working simultaneously, it is recommended you first establish a firm knowledge of git. A good tutorial can be found at http://www.vogella.com/articles/Git/article.html.

## Setting up the environment ##
Make sure you have eclipse installed with the [Google Plugin](https://developers.google.com/eclipse/docs/download). Download and import Clashroom as an existing project.

If you get buildpath errors, go to Properties->Java Build Path and remove the "App Engine SDK 1.x.x" from your build path. Then go the Properties->Google->App Engine and make sure your version of the App Engine SDK is checked. Checking and unchecking this the "Use Google App Engine" option may also help.

Launch Clashroom as a "Web Application."

## Using Git ##
For single contributors, the GitHub client will likely be sufficient for pushing and pulling the repository. For multiple contributors, consider using the [EGit](http://www.eclipse.org/egit/) plugin for eclipse. We have found the best strategy is to have each contributor create a new branch to work in. When the branches need to be merged, we recommend merging them into the master one at a time. First switch to the master, then merge in the first branch. Subsequent branches will involve conflict resolution, so use Team->Advanced->Synchronize->origin/[branch to merge in] to manually synchronize the branches. After each successful merge, make sure to test and then commit and push to save your work.

## Structure ##
Like any GWT application, code is divided into 3 main packages: client, server and shared.

### Client ###
The client contains the UI for Clashroom. The Clashroom class serves as the entrypoint for the application. The UI is split into various Page classes, each of which represents a page on the website. Page navigation is handled by the FlowControl class. The HomePage class is the main page, and contains links to the various other portions of the website. These are generally separated into the other sub-packages in the client folder:

**window** contains various Window subclasses, which are the various sections of the HomePage. Most Windows have a corresponding Page where their information can be viewed in more detail.

**widget** contains custom GWT widgets for use on the various pages.

**user** contains pages dealing with the user, viewing a user's profile and setting up a new user.

**quest** contains pages dealing with real-world quests.

**battle** contains pages dealing with battles between players, including a page which uses HTML5 canvas to display battles to the user.

**bounty** contains pages dealing with bounties, small time-based side quests that the user can queue up to gain extra experience. This rewards players who check in frequently to the website.

**teacher** contains pages that the teacher might use to manipulate the game as time progresses.

### Server ###
The server contains the backend of Clashroom, mainly handling requests to the datastore. The main package contains a few servlets and utilities. The impl subpackage contains implementations for the [RPC server calls](https://developers.google.com/web-toolkit/doc/latest/tutorial/RPC).

### Shared ###
Shared contains code used on the client and server sides, including a number of serializable classes which are passed over RPC back and forth between them. It is split into a number of sub-packages as well:

**battle** contains classes for the simulated battles between Dragons. Inside are sub-packages for classes of Dragons, skills for battlers, types of battlers, buffs which can be applied to battlers and actions which can occur during battles.

**entity** contains entities which can be stored in the datastore. Many of these entities are also serializable so they can be passed back to the client.

**news** contains types of news which can appear on a user's newsfeed, such as a battle occurring, a quest being completed, a bounty finishing, etc.

**task** contains special classes for dealing with the bounty system.

### Other resources ###
There are other resources under the war/ directory that are worth noting:

**css** contains the four css pages that are used to style Clashroom. The css is generally organized by category into different files.

**img** contains image resources used in Clashroom.

## Superclasses ##
Clashroom has a number of extendable superclasses which allow you to easily add components. The primary documentation can be found in the classes themselves, but some suggestions are as follows:

**Page.java**: Extend this class to add a page Clashroom. Make sure you register your page with the FlowControl.go() method.

**NewsfeedItem.java**: Extend this class to create a new type of news to appear in the Town Herald. Simply create a new NewsfeedEntity and pass your newly created NewsfeedItem to its constructor, then persist the entity.

**BattleAction.java**: Extend this class to create a new action that can occur during Battles. Current examples include attacking, dying, winning, etc. Make sure that BattlePage is edited to handle your action when it pops up.

**Battler.java**: Extend this class to create new types of NPC Battlers.

**Buff.java**: Extend this class to create a new type of Buff that can be applied to Battlers to change their stats.

**DragonClass.java**: Extend this class to create a new class of dragon for the player to use. Make sure you register your class in DragonClass's dragons field.

**Skill.java**: Extend this class to create a new Skill for dragons to use. Have a DragonClass add it to its skill tree to have that dragon be able to learn your skill. Make sure to register your new skill with Skill's skills array.

## TODO ##
There is a [TODO.md](TODO.md) file inside the main directory with future work that needs doing. There are also TODO markers inside the code with specific improvement that are needed.

## Helpful Hints ##
There are a number of things to watch out for when working with GWT. A few memorable ones are listed below:
 * Make sure any class that is being passed over RPC is Serializable, has a no-args constructor and doesn't include any fields that don't meet that requirement
 * If you want to send an entity retrieved from the datastore over RPC, you to send a PersistenceManager.detachCopy(entitiy) instead.
 * In order for JDO to recognize changes you make to an entity and persist them in the datastore, you have to use the appropriate setters. You can't just modify the field. For that reason, entities should never have public fields.
 * Sometimes changes you make to an entity won't persist in the datastore, even when you use the right setters. Sometime you can fix this by detaching a copy and repersisting it.
 * When you run into a problem, **just restart the server first**, then see if it persists before doing too much debugging.
 * [StackOverflow](http://www.stackoverflow.com) is your best friend. Use it.
 * Commit to GitHub often - it helps to have a record of you recent code.
 * Update the documentation! Even if it's only the last week of your work, make sure not to leave loose ends. Update this README to help future contributors.
