[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ac29bd747a6d48f7a70f79e67feb768b)](https://www.codacy.com/gh/Afanas10101111/democracy-for-lunch/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Afanas10101111/democracy-for-lunch&amp;utm_campaign=Badge_Grade)

Democracy for lunch
========
This application provide voting system for deciding where to have lunch today.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day
  * If it is before 11:00 we assume that he changed his mind.
  * If it is after 11:00 then it is too late, vote can't be changed
    
Each restaurant provides a new menu each day. Admins are responsible for updating the menu.

After the application is launched (for example, on port 8080), REST API documentation is available by the <a href="http://localhost:8080/swagger-ui.html">link</a> on the <a href="http://localhost:8080/">welcome page</a>.
