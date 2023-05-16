Enter your student ID (including the upper case B at the beginning, no spaces):
F120840

Does your app successfully compile in Android Studio?
Yes

Does your app correctly start on an emulator (without crashing)?
Yes

Which programming language are you using?
Java

What is the minimum API level of your app? (14 - 32)
31

What is the highest (i.e., latest) API level which you tested your app with? (14 - 30)
33

Which of the following screen types does your app support and has been tested on (select all which apply)?
- [x] Phone portrait
- [] Phone landscape
- [] Tablet portrait
- [] Tablet landscape
- [] Other

If you selected "Other" at the supported screen types, please give a short description:
TODO


Did you manually edit the gradle file of your Android project?
Yes

If you edited the gradle file, please give a short description of the changes.
Added libraries, fixed dependency conflicts.

Use at most 100 characters to describe the kind of app you implemented (e.g., "todo list" or "fitness tracker").
Social media app for shoes.

Does your app implement the functionalities of your specification in week 5?
TODO
- [ ] Yes, it satisfies all aspects of the specification.
- [ ] Yes, with only minor changes to the functionalities.
- [ ] No, the feedback for my original specification suggested to change the specification.
- [ ] No, I decided to change the specification.

If you were allowed to change your specification, did you submit a new specification by the end of week 8?
- [x] Does not apply
- [ ] Yes
- [ ] No

If your app does not implement all aspects of the specification in week 5 (or week 8), give a short reason why.
TODO (probably empty)

If you added features to the specification in week 5 (or week 8), give a short description:
TODO (i think added comments, need to check)

Is your app using a login mechanism?
- [x] Yes
- [ ] No

If your app is using a login mechanism, is there a test user "eee" with password "eee"?
- [ ] Does not apply
- [ ] Yes
- [x] No

Please specify any other test users (login/password/role/etc.).
eee@eee.com / eeeeee
aaa@aaa.com / aaaaaa
bbb@bbb.com / bbbbbb

Does your app have a minimum of two distinct screens (excluding the user guide and potential login screens)?
- [x] Yes
- [ ] No

(a) List the names of the xml layout files of all your screens (including potential login screens, one per line) and indicate potential qualifier values which are implemented in the design:
activity_login.xml
activity_main.xml
activity_main_navigation.xml
activity_new_post.xml
activity_post.xml
activity_user_profile.xml
fragment_home.xml
fragment_messages.xml
fragment_profile.xml
item_comment.xml
item_post.xml
item_post_carousel.xml
item_post_preview.xml
item_profile_preview.xml
view_dynamic_posts.xml
view_post_actions.xml
view_post_caption.xml
view_progress.xml
TODO: activity_conversation/activity_messsage

(a) List the names of the class files of all your screens (including potential login screens, one per line), indicate whether it is an activity or a fragment, and give the corresponding xml layout file names:
Base package: com.styleshow
.MainActivity.java, activity, activity_main.xml
.ui.login.LoginActivity.java, activity, activity_login.xml
.ui.MainNavigationActivity.java, activity, activity_main_navigation.xml
.ui.home.HomeFragment.java, fragment, fragment_home.xml
.ui.messages.MessagesFragment.java, fragment, fragment_messages.xml
.ui.profile.ProfileFragment.java, fragment, fragment_profile.xml
.ui.post.PostActivity.java, activity, activity_post.xml
.ui.new_post.NewPostActivity.java, activity, activity_new_post.xml
.ui.user_profile.UserProfileActivity.java, activity, activity_user_profile.xml
TODO: activity_conversation/activity_messsage

(a) Which APIs, libraries or third party code did you use when implementing feature a (list one per line)?
RecyclerView
Material Components for Android

Does your app work properly with the lifecycle (including rotate screen changes)?
- [x] Yes
- [ ] No

(b) List the names of the class files which implement non-trivial behaviour (i.e., not just calling the respective super method) for at least one of the lifecycle methods onStart(), onResume(), onPause(), onStop(), onDestroy():
TODO: all activities & fragments?

(b) Which APIs, libraries or third party code did you use when implementing feature b (list one per line)?


Does your app use permissions and use them responsibly?
- [x] Yes
- [] No

(c) List the permissions which your app uses (one per line); for each permission give the name of at least one method (including the name of its class) where this permission is required.
INTERNET - .data.remote.LoginDataSource#login
READ_MEDIA_IMAGES - new post activity
TODO

(c) For each of the above permissions, indicate whether your app checks that it has not been revoked before using it.
INTERNET - no
TODO

(c) Which APIs, libraries or third party code did you use when implementing feature c (list one per line)?
TODO

Does your app use intents to move inside your app?
- [x] Yes
- [ ] No

(d) For each intent moving inside your app, give the name of the method (including the name of its class) starting the intent as well as the name of the action which is started by it (one per line).
(d) Which APIs, libraries or third party code did you use when implementing feature d (list one per line)?

Does your app use intents to move to an outside app?
- [x] Yes
- [ ] No

(e) For each intent moving to an outside app, give the name of the method (including the name of its class) starting the intent as well as the name of the action which is started by it (one per line).
(e) Which APIs, libraries or third party code did you use when implementing feature e (list one per line)?

Did you create and use your own content provider in your app?
- [ ] Yes
- [ ] No

(f) List all the class files involved in the implementation of your content provider.
(f) List the names of all methods (including their class name) which are accessing the content provider (one per line).
(f) Which APIs, libraries or third party code did you use when implementing feature f (list one per line)?

Does your app use SharedPreferences?
- [x] Yes
- [ ] No

(g) List the names of all methods (including their class name) which are using SharedPreferences (one per line).
TODO: DynamicPostsView.java

(g) Which APIs, libraries or third party code did you use when implementing feature g (list one per line)?

Did you create and use a local database in your app?
- [ ] Yes
- [x] No

(h) List all the class files involved in the implementation of your local database(s).
(h) List the names of all methods (including their class name) which are directly accessing the local databases (and not indirectly via, e.g., a content provider; one per line).
(h) Which APIs, libraries or third party code did you use when implementing feature h (list one per line)?
Is your app using firebase for storing and retrieving data?

Is your app using firebase for storing and retrieving data?<i class="icon fa fa-exclamation-circle text-danger fa-fw " title="Required field" aria-label="Required field"></i>

Yes

No

(i) List the names of all methods (including their class name) which are directly accessing firebase (one per line).
(i) Which APIs, libraries or third party code did you use when implementing feature i (list one per line)?
Does your app receive Broadcast events and make use of them in meaningful ways?

Does your app receive Broadcast events and make use of them in meaningful ways?<i class="icon fa fa-exclamation-circle text-danger fa-fw " title="Required field" aria-label="Required field"></i>

Yes

No

(j) List the names of all your class files which are receiving Broadcast events; also list the type of the event (one per line).
(j) Which APIs, libraries or third party code did you use when implementing feature j (list one per line)?
Did you extend and use an existing View class in your app?

Did you extend and use an existing View class in your app?<i class="icon fa fa-exclamation-circle text-danger fa-fw " title="Required field" aria-label="Required field"></i>

Yes

No

(k) List the names of all your class files which extend an existing View class; also list the name of the View class it extends (one per line).
(k) Which APIs, libraries or third party code did you use when implementing feature k (list one per line)?
Did you use a ShareActionProvider in your app?

Did you use a ShareActionProvider in your app?<i class="icon fa fa-exclamation-circle text-danger fa-fw " title="Required field" aria-label="Required field"></i>

Yes

No

(l) List the names of all methods (including their class name) which using a ShareActionProvider (one per line).
(l) What kind of information are you sharing via the ShareActionProvider? Keywords are sufficient in your answer.
(l) Which APIs, libraries or third party code did you use when implementing feature l (i.e., feature "ell"; list one per line)?
Did you implement and use your own service in your app?

Did you implement and use your own service in your app?<i class="icon fa fa-exclamation-circle text-danger fa-fw " title="Required field" aria-label="Required field"></i>

Yes

No

(m) List the names of all your class files implementing a service (one per line).
(m) Give a quick description of the purpose of your own services and their use.
(m) Which APIs, libraries or third party code did you use when implementing feature m (list one per line)?
Does your app use the AlarmManager?

Does your app use the AlarmManager?<i class="icon fa fa-exclamation-circle text-danger fa-fw " title="Required field" aria-label="Required field"></i>

Yes

No

(n) List the names of all methods (including their class name) which use the AlarmManager (one per line).
(n) Which APIs, libraries or third party code did you use when implementing feature n (list one per line)?
Does your app use Notifications?

Does your app use Notifications?<i class="icon fa fa-exclamation-circle text-danger fa-fw " title="Required field" aria-label="Required field"></i>

Yes

No

(o) List the names of all methods (including their class name) which use Notifications (one per line).
(o) Give a quick description of the kind of information given in the notifications. Also say when the notifications are sent out (e.g., "when button XYZ is pressed" or "weekly, at a time set by the user").
(o) Which APIs, libraries or third party code did you use when implementing feature o (list one per line)?
Does your app capture touch gestures and make reasonable use of them?

Does your app capture touch gestures and make reasonable use of them?<i class="icon fa fa-exclamation-circle text-danger fa-fw " title="Required field" aria-label="Required field"></i>

Yes

No

(p) List the names of all your own methods and classes which capture touch gestures. Which kind of touch gesture are they capturing and what is the action performed?
(p) Which APIs, libraries or third party code did you use when implementing feature p (list one per line)?
Did you create a user guide and upload it in the assets folder of your app?

Did you create a user guide and upload it in the assets folder of your app?<i class="icon fa fa-exclamation-circle text-danger fa-fw " title="Required field" aria-label="Required field"></i>

Yes

No

How many different pages (e.g., html files) does you user guide consist of?
What is the name of the method (including its class name) which starts the WebView containing the user guide?
List up to 6 features of your user guide which make it responsive (keywords are sufficient; one per line).
Apart from the ones mentioned above, which other APIs, libraries or third party code did you use in your app (write "none" or list one per line)?
What kind of systematic testing did you do (e.g., none, unit testing of all methods, unit testing of some methods, integration testing, system testing, etc.)?
Did you use any frameworks for testing (e.g., JUnit, TestNG, Mockito, etc.)? If yes, which ones?
This is the place for providing any additional information which is necessary for testing and marking your app.
