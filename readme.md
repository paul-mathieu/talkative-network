# Project ePapotage

### Important

You may be missing elements from the commons codec library of apache.org, you can add in the parameters of your project the .jar file available in the talkative-network\lib directory

## Sujet

The goal is to create an offline instant messaging application using all our knowledge acquired in Java.
This instant messaging application is called ePapotage and should allow bavards to post messages and other bavards to listen to them if they want. The users of this system are therefore a concierge as well as several bavards. Each bavard can register with the manager to post messages and receive messages from other bavards. Communication between these users will be done using the event mechanism. Bavards and the concierge will exchange PapotageEvent. In response to the receipt of this message, the concierge forwards it to all connected bavards.

## Application architecture

My application is therefore composed of a graphical interface which is displayed when the application is launched. This interface allows you to create an account or connect to an already existing account. There is a single de ‘concierge’ type account, which allows you to view the logs of events and actions of different users. All other accessible accounts are chatter accounts, users who can send or listen to messages. All messages pass virtually through the concierge without being displayed, for the sake of confidentiality towards other users.