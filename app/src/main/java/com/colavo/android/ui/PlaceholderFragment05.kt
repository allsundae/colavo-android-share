package com.colavo.android.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.colavo.android.R
import com.colavo.android.R.menu.menu_customer
import com.colavo.android.R.string.bottom_navi_5
import com.colavo.android.base.BaseFragment
import com.colavo.android.common.MyTextView
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.entity.session.User
import com.colavo.android.ui.salons.SalonListActivity
import com.colavo.android.utils.Logger
import kotlinx.android.synthetic.main.toolbar.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener







/**
 * Created by macbookpro on 2017. 9. 13..
 */
class PlaceholderFragment05 : BaseFragment() {

/*    override fun getLayout(position: Int) = when (position) {
        1 -> R.layout.fragment_01
        2 -> R.layout.fragment_02
        3 -> R.layout.fragment_03
        4 -> R.layout.fragment_04
        else -> R.layout.fragment_05
    }*/
    override fun getLayout() = R.layout.fragment_05

    companion object {
        fun newInstance() = PlaceholderFragment05()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar?.setTitle (bottom_navi_5)
        toolBar?.inflateMenu(menu_customer)


        val salonTitle: MyTextView = (activity as AppCompatActivity).findViewById(R.id.settings_salon_name) as MyTextView
        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_CONVERSATION) as SalonModel
        salonTitle.text = salon.name


        val userName: MyTextView = (activity as AppCompatActivity).findViewById(R.id.settings_user_name) as MyTextView

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("users").child(user.uid)

            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl
            val uid = user.uid


            if (name != "" && name != null ) {
                userName.text =  name
            }
            else {
                userName.text = email
            }

            Logger.log("LOGGED IN NAME : ${name}")
            Logger.log("LOGGED IN NAME : ${userName.text}")

            /*          myRef.addListenerForSingleValueEvent(
                  object : ValueEventListener {
                      override fun onDataChange(dataSnapshot: DataSnapshot) {
                           userclass = dataSnapshot.getValue(User::class.java)

                          if (name != "" || name != null )
                              userName.text =  userclass.name
                          else
                              userName.text = email

                          Logger.log("LOGGED IN NAME : ${userclass.name}")
                      }

              override fun onCancelled(error: DatabaseError) {
                  Logger.log("Failed to read value. : ${error.toException()}")
              }
          })*/
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_customer, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_create_customer -> {
                //TODO create a customer
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }


}