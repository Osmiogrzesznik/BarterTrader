# Barter Trader Android App

You don’t need your old antimatter generator? You Dream about new Flux
Spanner and don’t mind if it has been used already?

Trade your second-hand goods by swapping it with the help of our
bombastic app.

“Barter Trader” has the purpose to help users to facilitate and make the
trade of different goods like clothes, gadgets, tools and toys.

This is university assignment project, but open-sourced , may conquer
the world , beating giants such as Ebay and Gumtree :-P
 

# Contents

[Abstract 3](#abstract)

[Overview of the project 3](#overview-of-the-project)

[Work Organisation 3](#work-organisation)

[Wireframing And Design 4](#wireframing-and-design)

[Software Architecture Elements 6](#software-architecture-elements)

[Firebase Data Modelling 7](#firebase-data-modelling)

[Testing plan 8](#testing-plan)

[Testing Screenshots 10](#testing-screenshots)

[SOURCE CODE 16](#_Toc26762213)

[Stress Testing and Surprising Problem
17](#stress-testing-and-surprising-problem)

[References: 18](#references)

# 

# 

# 

# 

# 

# 

# 

# 

# 

# 

# 

# 

# Abstract

This work aims to present process of developing a mobile application for
android system Also, the users have the option to rate the Seller or
post comments. This helps users wishing to trade goods which were the
experience of other users.

# Overview of the project

Our project for Mobile App Design and Development was to build a mobile
app called “Barter Trader” which has the purpose to help users to
facilitate and make the trade of different goods like clothes, gadgets,
tools and toys.

In order for users to be able to make trade between them, they need to
register an account. Furthermore, they need to upload a list of items
which they wish to trade. Once an user has decided to “purchase” a
product, he needs either to message the seller or send an offer
directly, which then needs to be accepted by both sides.

Also, the users have the option to rate the Seller or post comments.
This helps users wishing to trade goods which were the experience of
other users.

# Work Organisation 

The development for this application firstly begun by splitting the
responsibilities within the team based on skills and knowledge. For
example, who had more knowledge about java or android studio, has worked
on the back end of the project. On the other hand, we had people working
on the front end of the project, researching, organising or project
management side. We started by organising brainstorming sessions, like
design discussion or revise of the wireframing built in order to define
the functionality and core features of the app.

As a project management tool, we have Trello to monitor the completeness
of the tasks within the team and Github to share the source code and
also, to keep track of the various changes that have been made. GitHub
is a branch-based workflow, excellent for teams and projects allowing to
work on code independently. Our team used GitHub because it provided us
with the necessary tools for easier collaboration and code sharing on
any device. We started by creating a branch of our project where we
could safely experiment and make changes. We added commits and used a
pull request to get feedback on our changes from the other team members.
After discuss and reviews, we merge our changes into our master branch
and deploy our code.

Trello is a management app that helped us to split the task into three
categories, like “To Do”, “In Progress” and “Done”. It also helped us to
track to who is assigned a task, what was the deadline to complete the
task as well to see in which stage the work was. This helped us to
organise the team and keep a steady workflow. For each stage, we added
cards representing the activities and the task required to complete the
project, i.e. wireframing, testing, XML coded activities.

![](./media/image1.png)

Figure 1. Debug Screen

We Used Debug screen that will be removed before deployment , allowing
to quickly and efficiently

Test functionalities. Additionally We used NodeJS and AdminSDK for
Firebase to generate proper amount of fake test data – users , items
(pictures scraped from selling websites) and make designing layouts
prepared for large lists of data, and unexpected drawbacks of it.

# Wireframing And Design

Wireframing is one of the most popular ways to design the structural
aspects of a website or a mobile app, that is aimed at capturing the
user experience. Wireframe could be thought of as a blueprint before the
visual design such as colours, logos, gradients and the actual content
is added (Brown,2011). The key pages of any website or mobile app are
usually developed using wireframes. The user interface components,
navigational menu and the way they act together are the most essential
elements of a wireframe layout (Garrett,2010). Since the user interface
elements of a mobile application forms a critical part of the user
experience, it is essential to understand how these interface elements
are arranged and what kind of input is expected from the user. As a part
of this assignment a mobile app for trade is to be developed. The
functionality of the key pages and the user interaction elements have
been designed using a wireframe platform called FluidUI.

User login page where the username is the email id and password has been
given. If the user is not registered, a signup option where the
registration page loads is present in the login page. Registration the
first name, the last name, the mobile number, the email address and a
password are asked. A ‘Confirm password’ option is also given. On
hitting ‘Register’ option the user becomes registered user if the email
address and mobile number are valid and unique.

![](./media/image2.png)

Figure B1. Wireframing and screen transitions.

The users can also use the app as guest user but a warning is provided
that they cannot use bargaining option and would not be able to rate
other users. A list like provision to choose primary categories to
barter is added from where the user can directly search for a product
has also been provided. Users can click on his profile image to check
his profile. A post new item page from where users can add a picture of
the product they intend to barter is also displayed. A page where the
current user can rate other sellers using stars is provided. The same
page has also got an option to flag users by clicking on the flag icon.

![](./media/image3.png)

Figure B2. Brainstorming – wireframing messages.

It is important to use wireframing before starting the coding activity.
Firstly, it will offer a clear picture about the way the application
will appear and its functionality. Secondly, the expectations and the
functions of the app are easy to establish on this stage and it
determines how every screen of the app is going to be designed.

#  Software Architecture Elements

Griffiths (2017) states that Activities are java android Classes
responsible for behaviour and controlling the state of an application
and binding data models to views. Static side of graphical layout of an
application views (buttons , inputs, images) is defined by XML files.
XML stands for Extensible Markup Language. Bray et al. (2008) define it
as new format allowing for designing human readable, quick , compatible
and versatile static data models. Contrary to programming language such
as Java allowing us to define behaviours, interactions, and conditions,
the role of a markup language is more about describing data relations
and strucutre. and, in this case, layouts. Programming languages
​​create dynamic interactions, while markup languages ​​generally
deal with things like static user interfaces.

Serializable interface for passing complete data objects from activity
to activity . Decision to use it over Parcelable was dictated by the
fact that latter is more complicated and oriented on performance
optimisation. Even though it is clearly more performant , analysis of
performance on development stage may lead to stagnation and complicating
the code on early stages according to Knuth(1974). Migration to
parcelable can be done on the later stage when application is finished
and assesed on performance.

Facade design pattern , was used to create easily expressed Firebase
calls, hiding singleton instantiations behind static calls to DB class.
Idea behind Façade pattern is expressed by Freeman et al.(2008) the way
to hide complex or unwieldy API behind interface accommodating needs of
a developer. All needed Firebase components , and calls to child
retrieval were shorter and clearer.

# Firebase Data Modelling

Firebase is a NoSQL type database, where data is stored in documents
called nodes. Smyth (2017) remarks that design and Modelling are
considerably different from relational SQL paradigm , using key – value
pairs where value can be list of keys or complete object. Since there is
no relations or schema embedded in data, client applications are obliged
to organize data manipulation in such a way that required data structure
emerges from it. To denormalize (flatten ) the structure we may have to
store list of keys repeatedly , depending on what views in our
application require. This leads to redundancies such as in our case
storing keys for inbox message-threads inside user object , which
translates roughly as foreign keys. Actual Messages are stored in the
separate node.

![](./media/image4.png) ![](./media/image5.png)

Figure A1. nodes at root Figure A2. User node with expanded inbox node,
and tradedWith

# Testing plan

This section of the project displays the plan and sequence for testing
the application. Our approach was to use an integration test. We started
by assigning this task to a couple of team members, prepare integration
test scenarios and test cases, execute the test cases and report
defects.

We started testing from launch page, dashboard and categories by using
two users in order to see how the trade has been implement and if all
the functionalities are working as we expecting. Along the way we have
discovered some bugs which that then have been fixed and also have
improved some functionalities (i.e prevent brute force attack).

<table>
<thead>
<tr class="header">
<th><em><strong>Sl. No</strong></em></th>
<th><em><strong>Test Case</strong></em></th>
<th><em><strong>Input</strong></em></th>
<th><em><strong>Expected Output</strong></em></th>
<th><em><strong>Note</strong></em></th>
<th><em><strong>Remarks</strong></em></th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td><p>1.</p>
<p>TC-GR</p></td>
<td>Go to register</td>
<td><p>Launch application&gt;</p>
<p>Click SignUp link&gt;</p></td>
<td>Register Page (figure TC-GR 1 &amp; 2)</td>
<td>This activity should allow the user to choose to register</td>
<td>Pass</td>
</tr>
<tr class="even">
<td><p>2.</p>
<p>TC-LF</p></td>
<td>Login Failed</td>
<td><p>Launch application &gt;</p>
<p>Enter:</p>
<p>Valid and registered email:</p>
<p><a href="mailto:test@test.com">test@test.com</a></p>
<p>Enter:</p>
<p>incorrect password</p></td>
<td><p>Not allowing user to login display message - failed</p>
<p>(figure TC-LF 1 &amp; 2)</p></td>
<td>For security purpose, the program won’t display what field was completed incorrectly, to avoid other user to discover the login details (prevent brute force attack).</td>
<td>Pass</td>
</tr>
<tr class="odd">
<td><p>3.</p>
<p>TC-RF</p></td>
<td>Register failed</td>
<td><p>Launch application &gt;Register &gt;</p>
<p>Various Empty or incorrect fields</p></td>
<td><p>Not allowing user to register display error message on the corresponding field</p>
<p>(figure TC-RF 1 - 4)</p></td>
<td>The program prompts you to fill the required fields when they are empty or display a hint when value is invalid.</td>
<td>Pass</td>
</tr>
<tr class="even">
<td><p>4.</p>
<p>TC-LS</p></td>
<td>Login succesful</td>
<td>Launch application &gt;Login Page &gt; My Profile (Correct information in the fields)</td>
<td><p>Should allow user to login</p>
<p>(figure TC-LS 1-4)</p></td>
<td>Once the login details have been correctly entered, the program will allow user to access My Profile / Dashboard screen.</td>
<td>Pass</td>
</tr>
<tr class="odd">
<td><p>5.</p>
<p>TC-CP</p></td>
<td>change / upload profile picture</td>
<td><p>Dashboard&gt;</p>
<p>My Profile&gt;</p>
<p>Profile picture&gt;</p>
<p>select Picture</p>
<p>(figures TC-CP 1-3)</p></td>
<td>Changed the user profile photo</td>
<td>When use click on profile photo has the option to upload a new photo from the device it is using</td>
<td>Pass</td>
</tr>
<tr class="even">
<td><p>6.</p>
<p>TC-IR</p></td>
<td><p>View inbox</p>
<p>Unread messages</p></td>
<td><p>Dashboard&gt;Inbox</p>
<p>OR</p>
<p>Dashboard&gt;</p>
<p>Mail Icon</p></td>
<td>Inbox (figure TC-IR 1-2).</td>
<td>This activity send the user to the messages list and it will display a difference between read / unread messages and message Offers Barter Trade</td>
<td>Pass</td>
</tr>
<tr class="odd">
<td><p>7.</p>
<p>TC-CH</p></td>
<td>Send Messages from inbox</td>
<td><p>Dashboard&gt;</p>
<p>Inbox &gt;</p>
<p>Inbox Element</p></td>
<td><p>Send messages</p>
<p>(figures TC-CH 1-3)</p></td>
<td>When user selects one of the conversation with other user from the list, is able to use the chat and text a new message.</td>
<td>Pass</td>
</tr>
<tr class="even">
<td><p>8.</p>
<p>TC-PO</p>
<p>8b.</p>
<p>TC-VID</p></td>
<td>Post Item</td>
<td><p>Dashboard&gt;My Profile &gt;Post new item</p>
<p>&gt; Enter item details &gt;Select file&gt;</p>
<p>Click post item</p></td>
<td><p>(figure TC-PO 1 &amp; 2 )</p>
<p>Optional video</p>
<p>(figures TC-VID 1- 7)</p></td>
<td>Here the user can enter the item details, upload photo and / or add a video, select the category from the drop down arrow, add the item name &amp; description and finally post the item on the market list.</td>
<td>Pass</td>
</tr>
<tr class="odd">
<td><p>9.</p>
<p>TC-</p>
<p>Search</p></td>
<td>Search Item by title</td>
<td><p>Dashboard&gt;</p>
<p>My Profile &gt;</p>
<p>Search Categories&gt;</p>
<p>Tools Category&gt;</p>
<p>Enter: “hammer and”</p></td>
<td>Narrowing list to Find a required product (figures TC-Search))</td>
<td>When user is using the tool bar, it can search a specific item using partial item name.</td>
<td>Pass</td>
</tr>
<tr class="even">
<td>10.</td>
<td></td>
<td>Dashboard&gt;My Profile &gt;Log out</td>
<td>Log out from application (figure 13)</td>
<td>By clicking on the log out button, the user will be disconnected from the app.</td>
<td>Pass</td>
</tr>
<tr class="odd">
<td>11.</td>
<td>Categories</td>
<td>Categories&gt;Search categories&gt;Msg. Seller</td>
<td>Send message to Seller (figure 14)</td>
<td>Here the user can search for items and he can contact the seller he wants to do the trade with by sending a mesg.</td>
<td>Pass</td>
</tr>
<tr class="even">
<td>12.</td>
<td></td>
<td>Categories&gt;Search categories&gt;Make an offer</td>
<td>Send trade offer (figure 15)</td>
<td>By clicking on the “make an offer” button, the user will select an item from his list of items and the next step will be accept to send that offer to the other user or cancel in case it change his mind.</td>
<td>Pass</td>
</tr>
<tr class="odd">
<td>13.</td>
<td></td>
<td>Categories&gt;Search categories&gt;View offer</td>
<td>View offer received (figure 16)</td>
<td>The other user is checking the inbox, open the unread message and can see what offer has received</td>
<td>Pass</td>
</tr>
<tr class="even">
<td>14.</td>
<td></td>
<td>Categories&gt;Search categories&gt;Accept Trade</td>
<td><p>Finalise trade (figure 17)</p>
<p>Item not in lists</p>
<p>Item displayed as already traded (figure 18)</p></td>
<td>Click on the received offer and click ok to accept and finalise the trade</td>
<td>Pass</td>
</tr>
<tr class="odd">
<td>15.</td>
<td></td>
<td>My items</td>
<td>View items</td>
<td>Once the trade has been accepted, items that have been traded will disappear from my items list</td>
<td>Pass</td>
</tr>
<tr class="even">
<td>16.</td>
<td></td>
<td>See my reviews</td>
<td>Access and see my reviews (figure 10)</td>
<td>User is able to check what reviews has received from his buyers</td>
<td>Pass</td>
</tr>
<tr class="odd">
<td>17.</td>
<td></td>
<td>View Seller reviews</td>
<td>See Seller reviews (figure 11)</td>
<td>Click on user stars, rating or see reviews link to see what other users comment and posted. Button for adding review not available as when no trade has been made with user</td>
<td>Pass</td>
</tr>
<tr class="even">
<td>18.</td>
<td></td>
<td><p>Trade with person&gt;</p>
<p>Go to their reviews&gt;</p></td>
<td>Redirected to reviews list and new review added</td>
<td>Button is only available to users that traded.</td>
<td>Pass</td>
</tr>
<tr class="odd">
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
</tr>
</tbody>
</table>

# Testing Screenshots

GO to register Login Failed

![](./media/image6.png) ![](./media/image7.png) ![](./media/image8.jpeg)
![](./media/image9.png)

TC-GR 1 TC-GR 2 TC-LF 1 TC-LF 2

Registration Failed – error messages

![](./media/image10.PNG) ![](./media/image11.png)
![](./media/image12.png) ![](./media/image13.png)

TC-RF 1 TC-RF 2 TC-RF 3 TC-RF 4

Successful Login takes to dashboard

![](./media/image14.PNG) ![](./media/image15.PNG)
![](./media/image16.png) ![](./media/image17.png)

TC-LS 1 TC-LS 2 TC-LS 3 TC-LS 4 update

Viewing Inbox

![](./media/image18.PNG) ![](./media/image19.png)
![](./media/image20.png)

TC-IR 1.read/unread TC-IR 2. new user TC-IR 3. Inbox new user

Sending Messages

![](./media/image21.png) ![](./media/image22.png)
![](./media/image23.png)

TC-CH 1 TC-CH 2 TC-CH 3

Add new item – no video

![](./media/image24.png)![](./media/image25.png)
![](./media/image26.png) ![](./media/image27.png)

TC-PO 1 TC-PO 2 TC-PO 3 TC-PO 4 and TC-Search

Add item with video

![](./media/image28.png) ![](./media/image29.png)
![](./media/image30.png) ![](./media/image31.jpeg)

TC-VID 1 TC-VID 2 category TC-VID 3 before selecting TC-VID 4 selecting
video

![](./media/image32.png) ![](./media/image33.png)
![](./media/image34.png)

TC-VID 5 uploading bar TC-VID 6 button for video TC-VID 7 video activity

![](./media/image35.PNG) ![](./media/image36.PNG)
![](./media/image37.PNG)

Figure 13 Figure 14 Figure 15

![](./media/image38.PNG) ![](./media/image39.PNG)
![](./media/image40.png)

Figure 16 Figure 17 Figure 18 already traded item message

Test Case : change / upload profile picture(test case TC-CP)

![](./media/image41.jpeg)![](./media/image42.jpeg)![](./media/image43.jpeg)

TC-CP1 TC-CP2 TC-CP3

TC-CP 1

![](./media/image44.png)

Figure 10 my Reviews

![](./media/image45.png)
![](./media/image46.png)![](./media/image47.png)![](./media/image48.png)

Figures 20,21,22,23 Trade Finalised \> Button Available \> Posting new
review\> new Review in list (bottom)

# Stress Testing and Surprising Problem

Unfortunately, extensive testing with multiple users lead to overusing
allowed bandwidth quota and hindered further progress on development

![](./media/image49.png)![](./media/image50.png)

![](./media/image51.png)

# References:

Android Developers (2019) Documentation. Available at:
<https://developer.android.com/reference>

Ferm, K. (2014). Developing Android application prototype for internal
social networks for companies. Available at:   
<http://www.diva-portal.org/smash/get/diva2:699480/FULLTEXT01.pdf>

Knuth, D. E. (1974). Computer programming as an art. *Communications of
the ACM*, *17*(12), 667-673. Available at:
<http://www.cs.lamar.edu/faculty/osborne/COSC1172/a1974-knuth.pdf>

Griffiths, D., & Griffiths, D. (2017). *Head First Android Development:
a brain-friendly guide*. " O'Reilly Media, Inc.".

Freeman, E., Robson, E., Bates, B., & Sierra, K. (2008). *Head first
design patterns*. " O'Reilly Media, Inc.".

Assmann, U. (1997). *AOP with design patterns as meta-programming
operators*. Technical Report 28, Universität Karlsruhe.

Smyth, N. (2017). *Firebase Essentials-Android Edition.\[Online\]
Payload Media*, 163-171.

Bray, T., Paoli, J., Sperberg-McQueen, C. M., Mailer, Y., & Yergeau, F.
(2008) Extensible Markup Language (XML) 1.0 5th Edition, W3C
recommendation, November 2008. Available
At:<https://www.w3.org/TR/REC-xml/>
