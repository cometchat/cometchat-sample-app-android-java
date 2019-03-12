package com.inscripts.cometchatpulse.demo.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Activity.OneToOneChatActivity;
import com.inscripts.cometchatpulse.demo.Adapter.ContactListAdapter;
import com.inscripts.cometchatpulse.demo.Contracts.ContactsContract;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.Helper.RecyclerTouchListener;
import com.inscripts.cometchatpulse.demo.Helper.ScrollHelper;
import com.inscripts.cometchatpulse.demo.Presenters.ContactsListPresenter;
import com.cometchat.pro.models.User;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment implements ContactsContract.ContactView{


    private ContactListAdapter contactListAdapter;

    private RecyclerView contactRecyclerView;

    private ImageView ivNoContacts;

    private TextView tvNoContacts;

    private ContactsContract.ContactPresenter contactPresenter;

    private ScrollHelper scrollHelper;

    private User user;

    private ShimmerFrameLayout contactShimmer;
    private LinearLayoutManager linearLayoutManager;

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        ivNoContacts = view.findViewById(R.id.ivNoContacts);
        tvNoContacts=view.findViewById(R.id.tvNoContacts);
        contactRecyclerView = view.findViewById(R.id.contacts_recycler_view);
        contactShimmer=view.findViewById(R.id.contact_shimmer);

        linearLayoutManager=new LinearLayoutManager(getContext());
        contactRecyclerView.setLayoutManager(linearLayoutManager);
        contactRecyclerView.setItemAnimator(new DefaultItemAnimator());

        contactPresenter = new ContactsListPresenter();

        contactPresenter.attach(this);

        contactPresenter.getLoggedInUser();

        contactPresenter.fecthUsers();

        contactRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                contactRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View var1, int position) {

                String contactID = (String) var1.getTag(R.string.contact_id);
                String contactName = (String) var1.getTag(R.string.contact_name);
                String userAvatar = (String) var1.getTag(R.string.user_avatar);
                User user= (User) var1.getTag(R.string.user);
                ContactListAdapter.ContactViewHolder contactViewHolder = (ContactListAdapter.ContactViewHolder) var1.getTag(R.string.userHolder);

                Pair<View,String > p1=Pair.create(((View)contactViewHolder.avatar),"profilePic");
                Pair<View,String > p2=Pair.create(((View)contactViewHolder.userName),"Name");
                Pair<View,String > p3=Pair.create(((View)contactViewHolder.userStatus),"status");
                Intent intent=new Intent(getContext(), OneToOneChatActivity.class);
                intent.putExtra(StringContract.IntentStrings.USER_ID,contactID);
                intent.putExtra(StringContract.IntentStrings.USER_AVATAR,userAvatar);
                intent.putExtra(StringContract.IntentStrings.USER_NAME,contactName);
                ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),p1,p2,p3);
                startActivity(intent,optionsCompat.toBundle());


            }

            @Override
            public void onLongClick(View var1, int position) {

            }
        }));

             contactRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                 @Override
                 public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                     super.onScrollStateChanged(recyclerView, newState);


                     if (!recyclerView.canScrollVertically(1)) {
                         contactPresenter.fecthUsers();
                     }

                 }

                 @Override
                 public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                     super.onScrolled(recyclerView, dx, dy);
                     if (dy < 0) {
                         scrollHelper.setFab(true);
                     } else
                         scrollHelper.setFab(false);
                 }
             });


        return view;
    }

    @Override
    public void updatePresence(User user) {
        if (contactListAdapter != null) {
            contactListAdapter.updateUserPresence(user);
        }
    }

    @Override
    public void setLoggedInUser(User user) {
        this.user=user;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contactPresenter.removePresenceListener(getString(R.string.presenceListener));
    }

    @Override
    public void onStart() {
        super.onStart();
        contactPresenter.addPresenceListener(getString(R.string.presenceListener));


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        scrollHelper = (ScrollHelper) context;
    }

    @Override
    public void setContactAdapter(List<User> userArrayList) {

        if (contactListAdapter == null) {
            contactListAdapter = new ContactListAdapter(user.getUid(),userArrayList, getContext(), R.layout.contact_list_item);
            contactRecyclerView.setAdapter(contactListAdapter);
            contactShimmer.stopShimmer();
            contactShimmer.setVisibility(View.GONE);
        } else {
            if (userArrayList != null) {
                contactListAdapter.refreshData(userArrayList);
            }
        }

        if (userArrayList != null && userArrayList.size() == 0) {
            tvNoContacts.setVisibility(View.VISIBLE);
            ivNoContacts.setVisibility(View.VISIBLE);
        } else {
            tvNoContacts.setVisibility(View.GONE);
            ivNoContacts.setVisibility(View.GONE);
        }

    }

}
