/**
 * QuestCreatorServlet.java 1.0 Mar 7, 2013	
 *
 * COPYRIGHT (c) 2013 Riese P. Narcisse. All Rights Reserved 
 */
package com.clashroom.server;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clashroom.shared.entity.QuestEntity;


/**
 * This is a servlet that will handle the form submition from the
 * quest creator page
 * 
 * @author Rpn
 * @version 1.0
 * 
 */
public class StoreQuestServlet extends HttpServlet {

    @SuppressWarnings("javadoc")
    @Override
    public void doGet(HttpServletRequest request,
                                    HttpServletResponse response)
                                    throws ServletException, IOException {
        QuestEntity newQuest = createQuest(request, response);
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(newQuest);
        } finally {
            pm.close();
        }
    }

    private QuestEntity createQuest(HttpServletRequest request,
                                    HttpServletResponse response) {
        String questName = request.getParameter("Quest Name");
        String questDesc = request.getParameter("Quest Description");
        String completionType = request.getParameter("Completion Type");
        String autoGenerateCode = request
                                        .getParameter("Auto Generate Code");
        String codeInput = null;
        int expGained = Integer.parseInt(request
                                        .getParameter("Awarded XP"));
        List<String> itemsRewarded = Arrays.asList(request.getParameter(
                                        "Items Selected").split(","));
        String questAvailability = request
                                        .getParameter("Quest Availability");
        String questUnAvailability = request
                                        .getParameter("Quest Unavailablility");
        String priorQuest = null;
        int requiredLevel = 0;
        /*
         * I don't think this takes into consideration daylight
         * savings
         */
        Date dateAvailable = new Date();
        Date dateUnavailable = new Date();
        DateFormat formatter = new SimpleDateFormat("MMM dd yyyy hh:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        String victoryText = request.getParameter("Victory Text");

        if (questAvailability.equals("DateTime")) {
            // TODO Get the appropriate Date
        } else {
            // TODO Just set the date to the current date
        }

        if (questAvailability.equals("Prior Quest")) {
            priorQuest = request.getParameter("Prereq Quest");
        } else {
            priorQuest = null;
        }

        if (questUnAvailability.equals("Time is a factor. There is much at stake.")) {
            // TODO Get the Appropriate Date
        } else {
            // TODO Just set the date to null, can do a check for it
            // later.
        }

        if (questAvailability.equals("Experience")) {
            requiredLevel = Integer.parseInt(request
                                            .getParameter("Level Requirement"));
        } else {
            requiredLevel = 0;
        }

        if (completionType.equals("Code")) {
            if (autoGenerateCode.equals("Yes")) {
                codeInput = generateRandomCode();
            } else {
                codeInput = request.getParameter("Code Input");
            }
        }
        QuestEntity newQuest = new QuestEntity(questName, questDesc, codeInput,
                                        expGained, requiredLevel,
                                        formatter.format(dateAvailable),
                                        formatter.format(dateUnavailable),
                                        victoryText, priorQuest,
                                        itemsRewarded);

        System.out.println(formatter.format(dateAvailable));
        return newQuest;

    }

    /*
     * Generates a 6 letter random code
     */
    private String generateRandomCode() {
        String randomString = "";

        for (int i = 0; i < 7; i++) {
            int intChar = 97 + (int) (Math.random() * ((122 - 97) + 1));
            char c = (char) intChar;
            randomString += c;
        }

        return randomString;
    }
}
