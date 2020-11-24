package pl.patrykdolata.chatapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.main_activity.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.adapters.PageAdapter
import pl.patrykdolata.chatapp.db.AppDatabase
import pl.patrykdolata.chatapp.fragments.ConversationsFragment
import pl.patrykdolata.chatapp.fragments.FriendsFragment
import pl.patrykdolata.chatapp.services.SocketService
import pl.patrykdolata.chatapp.utils.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: AppDatabase
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        db = AppDatabase(this)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        if (user != null) {
            userTextView.text = user!!.email
        } else {
            userTextView.visibility = View.GONE
            logout()
            return
        }

        mainToolbar.title = "ChatApp"
        setSupportActionBar(mainToolbar)

        setupViewPager()
        when (intent.extras?.getString("fragment", "conversations")) {
            "conversations" -> viewPager.currentItem = 0
            "friends" -> viewPager.currentItem = 1
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        println("destroy main")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logoutAction -> logout(true)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout(onDemand: Boolean = false) {
        if (onDemand) {
            SocketService.emit(Constants.LOGOUT_EVENT, user!!.uid)
            auth.signOut()
        }
        val intent = Intent(this, LoginActivity::class.java)
        Handler().postDelayed(
            {
                startActivity(intent)
                finish()
            },
            10
        )
    }

    private fun setupViewPager() {
        val adapter = PageAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        adapter.addFragment(
            ConversationsFragment(db, user!!.uid),
            getString(R.string.conversationsFragmentTitle)
        )
        adapter.addFragment(FriendsFragment(user!!.uid), getString(R.string.friedsFragmentTitle))
        viewPager.adapter = adapter

        tabs.setupWithViewPager(viewPager)
    }
}
