# Clashroom #

## About Clashroom ##
Clashroom is online, multiplayer RPG where players take on the role of Dragon trainers. Players take on real-world quests to give their Dragons experience, items and new skills. When played in the context of a classroom setting, the real-world quests can be things like getting help from a tutor, doing practice problems or doing supplementary research. Dragons face off against each other in the arena, so these quests become vital to success in the game. The hope is that adding a Clashroom layer to a class can encourage students to get involved in the subject outside of class in fun, voluntary way.

## Prerequisites ##
Clashroom is coded using Google Web Toolkit with a JDO/App Engine backend. Knowledge of each of these technologies is necessary to contribute to Clashroom. See https://developers.google.com/web-toolkit/ for a starting guide to GWT.

An understanding of git is also important for contributing. While a single contributor will be successful using the GitHub client, if multiple contributors are working simultaneously, it is recommended you first establish a firm knownledge of git. A good tutorial can be found at http://www.vogella.com/articles/Git/article.html.

## Setting up the environment ##
Make sure you have eclipse installed with the [Google Plugin](https://developers.google.com/eclipse/docs/download). Download and import Clashroom as an existing project.

If you get buildpath errors, go to Properties->Java Build Path and remove the "App Engine SDK 1.x.x" from your build path. Then go the Properties->Google->App Engine and make sure your version of the App Engine SDK is checked. Checking and unchecking this the "Use Google App Engine" option may also help.

Launch Clashroom as a "Web Application."

## Using Git ##
For single contributors, the GitHub client will likely be sufficient for pushing and pulling the repository. For multiple contributors, consider using the [EGit](http://www.eclipse.org/egit/) plugin for eclipse. We have found the best strategy is to have each contributor create a new branch to work in. When the branches need to be merged, we recommend merging them into the master one at a time. First switch to the master, then merge in the first branch. Subsequent brnaches will involved conflict resolution, so use Team->Advanced->Synchronize->origin/[branch to merge in] to manually synchronize the branches. After each successful merge, make sure to test and then commit and push to save your work.

## Structure ##
Like any GWT application, code is divided into 3 main packages: client, server and shared.

### client ###
The client contains the UI for Clashroom. The Clashroom class serves as the entrypoint for the application. The UI is split into various Page classes, each of which represents a page on the website. Page navigation is handled by the FlowControl class. The HomePage class is tha main page, and contains links to the various other portions of the website. These are generally separated into the other sub-packes in the client folder:

##### window #####
This contains various Window subclasses, which are the various sections of the HomePage. Most Windows have a corresponding Page where their information can be viewed in more detail.

##### widget #####
This contains custom GWT widgets for use on the various pages.

##### user #####
This contains pages dealing with the user, viewing a user's profile and setting up a new user.

##### quest #####
This contains pages dealing with real-world quests.

##### battle #####
This contains pages dealing with battles between players, including a page which uses HTML5 canvas to display battles to the user.

##### bounty #####
This contains pages dealing with bounties, small time-based side quests that the user can queue up to gain extra experience. This rewards players who check in frequently to the website.

##### teacher #####
This contains pages that the teacher might use to manipulate the game as time progresses.
