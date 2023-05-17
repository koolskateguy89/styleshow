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
- [x] Phone landscape
- [x] Tablet portrait
- [x] Tablet landscape
- [] Other

If you selected "Other" at the supported screen types, please give a short description:
NONE

Did you manually edit the gradle file of your Android project?
Yes

If you edited the gradle file, please give a short description of the changes.
Added libraries, fixed dependency conflicts.

Use at most 100 characters to describe the kind of app you implemented (e.g., "todo list" or "fitness tracker").
Social media app for shoes.

Does your app implement the functionalities of your specification in week 5?
- [ ] Yes, it satisfies all aspects of the specification.
- [x] Yes, with only minor changes to the functionalities.
- [ ] No, the feedback for my original specification suggested to change the specification.
- [ ] No, I decided to change the specification.

If you were allowed to change your specification, did you submit a new specification by the end of week 8?
- [x] Does not apply
- [ ] Yes
- [ ] No

If your app does not implement all aspects of the specification in week 5 (or week 8), give a short reason why.
Features including updating user profiles and posts are not implemented. They are not key features of the app and I did not have sufficient time to implement it.

If you added features to the specification in week 5 (or week 8), give a short description:
NONE

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
ccc@ccc.com / cccccc
ddd@ddd.com / dddddd

Does your app have a minimum of two distinct screens (excluding the user guide and potential login screens)?
- [x] Yes
- [ ] No

(a) List the names of the xml layout files of all your screens (including potential login screens, one per line) and indicate potential qualifier values which are implemented in the design:
activity_chat.xml
activity_chat.xml  (land)
activity_login.xml
activity_register.xml
activity_main.xml
activity_main_navigation.xml
activity_new_post.xml
activity_new_post.xml (land)
activity_post.xml
activity_post.xml (land)
activity_user_profile.xml
activity_guide.xml
fragment_home.xml
fragment_home.xml (land)
fragment_messages.xml
fragment_messages.xml (land)
fragment_profile.xml

(a) List the names of the class files of all your screens (including potential login screens, one per line), indicate whether it is an activity or a fragment, and give the corresponding xml layout file names:
Base package: com.styleshow
.MainActivity, activity, activity_main.xml
.ui.login.LoginActivity, activity, activity_login.xml
.ui.register.RegisterActivity, activity, activity_register.xml
.ui.MainNavigationActivity, activity, activity_main_navigation.xml
.ui.home.HomeFragment, fragment, fragment_home.xml
.ui.messages.MessagesFragment, fragment, fragment_messages.xml
.ui.profile.ProfileFragment, fragment, fragment_profile.xml
.ui.post.PostActivity, activity, activity_post.xml
.ui.new_post.NewPostActivity, activity, activity_new_post.xml
.ui.user_profile.UserProfileActivity, activity, activity_user_profile.xml
.ui.chat.ChatActivity, activity, activity_chat.xml
.ui.guide.GuideActivity, activity, activity_guide.xml

(a) Which APIs, libraries or third party code did you use when implementing feature a (list one per line)?
RecyclerView
Material Components for Android

Does your app work properly with the lifecycle (including rotate screen changes)?
- [x] Yes
- [ ] No

(b) List the names of the class files which implement non-trivial behaviour (i.e., not just calling the respective super method) for at least one of the lifecycle methods onStart(), onResume(), onPause(), onStop(), onDestroy():
Base package: com.styleshow
.MainActivity
.ui.login.LoginActivity
.ui.register.RegisterActivity
.ui.MainNavigationActivity
.ui.home.HomeFragment
.ui.messages.MessagesFragment
.ui.profile.ProfileFragment
.ui.post.PostActivity
.ui.new_post.NewPostActivity
.ui.user_profile.UserProfileActivity
.ui.chat.ChatActivity
.ui.guide.GuideActivity

(b) Which APIs, libraries or third party code did you use when implementing feature b (list one per line)?

Does your app use permissions and use them responsibly?
- [x] Yes
- [] No

(c) List the permissions which your app uses (one per line); for each permission give the name of at least one method (including the name of its class) where this permission is required.
Base package: com.styleshow
INTERNET - .data.remote.LoginDataSource#login
READ_MEDIA_IMAGES - .ui.new_post.NewPostActivity#openImagePicker
READ_EXTERNAL_STORAGE - .ui.new_post.NewPostActivity#openImagePicker
RECEIVE_BOOT_COMPLETED - .BootCompletedReceiver#onReceive
POST_NOTIFICATIONS - .NotificationReceiver#onReceive
WAKE_LOCK - .service.MessageNotificationService#onCreate

(c) For each of the above permissions, indicate whether your app checks that it has not been revoked before using it.
INTERNET - no
READ_MEDIA_IMAGES - no
READ_EXTERNAL_STORAGE - no
RECEIVE_BOOT_COMPLETED - no
POST_NOTIFICATIONS - yes
WAKE_LOCK - no

(c) Which APIs, libraries or third party code did you use when implementing feature c (list one per line)?
https://firebase.google.com/docs/cloud-messaging/android/client#request-permission13

Does your app use intents to move inside your app?
- [x] Yes
- [ ] No

(d) For each intent moving inside your app, give the name of the method (including the name of its class) starting the intent as well as the name of the action which is
started by it (one per line).
Base package: com.styleshow
.MainActivity#onCreate - explicit intent
.ui.login.LoginActivity#updateUiWithUser - explicit intent
.ui.login.LoginActivity#openRegisterActivity - explicit intent
.ui.MainNavigationActivity#openNoNetworkActivity - explicit intent
.ui.home.HomeFragment#launchPostActivity - activity launcher
.ui.home.HomeFragment#openNewPostActivity - activity launcher
.ui.home.HomeFragment#openProfileActivityForAuthor - explicit intent
.ui.messages.MessagesFragment#openChat - explicit intent
.ui.new_post.NewPostActivity.NewPostContract#createIntent - explicit intent (activity launcher result)
.ui.post.PostActivity.OpenPostContract#createIntent - explicit intent (activity launcher result)
.ui.post.user_profile.UserProfileActivity#launchPostActivity - activity launcher
.ui.profile.ProfileFragment#openUserGuideActivity - explicit intent
.ui.profile.ProfileFragment#launchPostActivity - activity launcher

(d) Which APIs, libraries or third party code did you use when implementing feature d (list one per line)?
NONE except ActivityResultLaunchers which are built into Android.

Does your app use intents to move to an outside app?
- [x] Yes
- [ ] No

(e) For each intent moving to an outside app, give the name of the method (including the name of its class) starting the intent as well as the name of the action which is started by it (one per line).
Base package: com.styleshow
.ui.post.PostActivity#showSharePopupMenu - "android.intent.action.VIEW"

(e) Which APIs, libraries or third party code did you use when implementing feature e (list one per line)?
NONE

Did you create and use your own content provider in your app?
- [ ] Yes
- [x] No

(f) List all the class files involved in the implementation of your content provider.
NONE

(f) List the names of all methods (including their class name) which are accessing the content provider (one per line).
NONE

(f) Which APIs, libraries or third party code did you use when implementing feature f (list one per line)?
NONE

Does your app use SharedPreferences?
- [x] Yes
- [ ] No

(g) List the names of all methods (including their class name) which are using SharedPreferences (one per line).
Base package: com.styleshow
.components.DynamicPostsView#init
.components.DynamicPostsView#getLayoutFromSharedPrefs
.components.DynamicPostsView#saveLayoutToSharedPrefs

(g) Which APIs, libraries or third party code did you use when implementing feature g (list one per line)?
SharedPreferences

Did you create and use a local database in your app?
- [ ] Yes
- [x] No

(h) List all the class files involved in the implementation of your local database(s).
NONE

(h) List the names of all methods (including their class name) which are directly accessing the local databases (and not indirectly via, e.g., a content provider; one per line).
NONE

(h) Which APIs, libraries or third party code did you use when implementing feature h (list one per line)?
NONE

Is your app using firebase for storing and retrieving data?
- [x] Yes
- [ ] No

(i) List the names of all methods (including their class name) which are directly accessing firebase (one per line).
Base package: com.styleshow
.remote.data.ChatDataSource#sendMessage
.remote.data.ChatDataSource#deleteMessage
.remote.data.ChatDataSource#listenForMessagesBetween
.remote.data.ChatDataSource#listenForMessageEvents

.remote.data.CommentDataSource#getCommentsForPost
.remote.data.CommentDataSource#postComment
.remote.data.CommentDataSource#deleteComment

.remote.data.LoginDataSource#getCurrentUser
.remote.data.LoginDataSource#login
.remote.data.LoginDataSource#logout

.remote.data.PostDataSource#getAllPosts
.remote.data.PostDataSource#getPostsByUser(UserProfile)
.remote.data.PostDataSource#getPostsByUser(String)
.remote.data.PostDataSource#likePost
.remote.data.PostDataSource#unlikePost
.remote.data.PostDataSource#publishPost
.remote.data.PostDataSource#deletePost

.remote.data.UserProfileDataSource#getAllProfilesExceptMe
.remote.data.UserProfileDataSource#getProfileForUid
.remote.data.UserProfileDataSource#getProfilesForUids
.remote.data.UserProfileDataSource#searchProfiles

(i) Which APIs, libraries or third party code did you use when implementing feature i (list one per line)?
Firebase Auth
Firebase Cloud Firestore
Firebase Cloud Storage

Does your app receive Broadcast events and make use of them in meaningful ways?
- [x] Yes
- [ ] No

(j) List the names of all your class files which are receiving Broadcast events; also list the type of the event (one per line).
Base package: com.styleshow
.BootCompletedReceiver - BOOT_COMPLETED

(j) Which APIs, libraries or third party code did you use when implementing feature j (list one per line)?
NONE

Did you extend and use an existing View class in your app?
- [x] Yes
- [ ] No

(k) List the names of all your class files which extend an existing View class; also list the name of the View class it extends (one per line).
Base package: com.styleshow
.components.DynamicPostsView - ConstraintLayout
.components.MovableFloatingActionButton - FloatingActionButton
.components.PicassoImageView - AppCompatImageView
.components.PostActionsView - ConstraintLayout
.components.PostCaptionView - ConstraintLayout
.components.SquareFrameLayout - FrameLayout

(k) Which APIs, libraries or third party code did you use when implementing feature k (list one per line)?
Picasso https://github.com/square/picasso
ConstraintLayout
MovableFloatingActionButton https://stackoverflow.com/a/46373935
SquareFrameLayout https://stackoverflow.com/a/24416992

Did you use a ShareActionProvider in your app?
- [x] Yes
- [ ] No

(l) List the names of all methods (including their class name) which using a ShareActionProvider (one per line).
Base package: com.styleshow
.ui.post.PostActivity#showSharePopupMenu

(l) What kind of information are you sharing via the ShareActionProvider? Keywords are sufficient in your answer.
Webpage like to a shoe.

(l) Which APIs, libraries or third party code did you use when implementing feature l (i.e., feature "ell"; list one per line)?
NONE

Did you implement and use your own service in your app?
- [x] Yes
- [ ] No

(m) List the names of all your class files implementing a service (one per line).
Base package: com.styleshow
.service.MessageNotificationService

(m) Give a quick description of the purpose of your own services and their use.
Service to listen for new messages and send notifications upon receiving them. Also cancels notifications when the message is deleted.

(m) Which APIs, libraries or third party code did you use when implementing feature m (list one per line)?
Firebase Firestore
AlarmManager

Does your app use the AlarmManager?
- [x] Yes
- [ ] No

(n) List the names of all methods (including their class name) which use the AlarmManager (one per line).
Base package: com.styleshow
.service.MessageNotificationService#scheduleNotification

(n) Which APIs, libraries or third party code did you use when implementing feature n (list one per line)?
NONE

Does your app use Notifications?
- [x] Yes
- [ ] No

(o) List the names of all methods (including their class name) which use Notifications (one per line).
Base package: com.styleshow
.service.MessageNotificationService#scheduleNotification
.service.MessageNotificationService#cancelNotification

(o) Give a quick description of the kind of information given in the notifications. Also say when the notifications are sent out (e.g., "when button XYZ is pressed" or "weekly, at a time set by the user").
When a new message is received, the sender's name and message are displayed in the notification.

(o) Which APIs, libraries or third party code did you use when implementing feature o (list one per line)?
Firebase Firestore

Does your app capture touch gestures and make reasonable use of them?
- [x] Yes
- [ ] No

(p) List the names of all your own methods and classes which capture touch gestures. Which kind of touch gesture are they capturing and what is the action performed?
Base package: com.styleshow
.components.MovableFloatingActionButton#onTouch - captures touch events to move the FAB around the screen
.adapters.ChatMessageAdapter.SentMessageHolder#SentMessageHolder - activate callback on long click
.adapters.ChatMessageAdapter.ReceivedMessageHolder#ReceivedMessageHolder - activate callback on long click
.adapters.CommentAdapter.CommentHolder#CommentHolder - activate callback on long click
.adapters.PostCarouselAdapter.PostHolder#PostHolder - activate callback on click
.adapters.PostPreviewAdapter.PostPreviewHolder#PostPreviewHolder - activate callback on click
.adapters.ProfilePreviewAdapter.ProfilePreviewHolder#ProfilePreviewHolder - activate callback on click

(p) Which APIs, libraries or third party code did you use when implementing feature p (list one per line)?
NONE

TODO:
Did you create a user guide and upload it in the assets folder of your app?
- [ ] Yes
- [ ] No

How many different pages (e.g., html files) does you user guide consist of?
1

What is the name of the method (including its class name) which starts the WebView containing the user guide?
com.styleshow.ui.guide.GuideActivity#onCreate

List up to 6 features of your user guide which make it responsive (keywords are sufficient; one per line).
Viewport units

Apart from the ones mentioned above, which other APIs, libraries or third party code did you use in your app (write "none" or list one per line)?
Material Components for Android
AndroidX Navigation UI
AndroidX Navigation Fragment
AndroidX Activity
AndroidX Preference
AndroidX Lifecycle Livedata
AndroidX Lifecycle Viewmodel
RxJava
RxAndroid
Timber
Picasso
Transformers
Dagger Hilt
Dagger Hilt Compiler

What kind of systematic testing did you do (e.g., none, unit testing of all methods, unit testing of some methods, integration testing, system testing, etc.)?
System testing

Did you use any frameworks for testing (e.g., JUnit, TestNG, Mockito, etc.)? If yes, which ones?
NONE

This is the place for providing any additional information which is necessary for testing and marking your app.
NONE
