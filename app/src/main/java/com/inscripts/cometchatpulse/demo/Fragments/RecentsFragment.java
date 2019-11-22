package com.inscripts.cometchatpulse.demo.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.helpers.CometChatHelper;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.inscripts.cometchatpulse.demo.Activity.GroupChatActivity;
import com.inscripts.cometchatpulse.demo.Activity.OneToOneChatActivity;
import com.inscripts.cometchatpulse.demo.Adapter.RecentsListAdapter;
import com.inscripts.cometchatpulse.demo.Contracts.RecentsContract;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.Helper.RecyclerTouchListener;
import com.inscripts.cometchatpulse.demo.Helper.ScrollHelper;
import com.inscripts.cometchatpulse.demo.Presenters.RecentsListPresenter;
import com.inscripts.cometchatpulse.demo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public class RecentsFragment extends Fragment implements RecentsContract.RecentsView{

    private RecentsListAdapter recentsListAdapter;

    private RecyclerView recentsRecyclerView;

    private ImageView ivNoRecents;

    private TextView tvNoRecents;

    private RecentsContract.RecentsPresenter recentPresenter;

    private ScrollHelper scrollHelper;

    private User user;

    private ShimmerFrameLayout recentShimmer;

    private LinearLayoutManager linearLayoutManager;
    private List<Conversation> conversationList=new ArrayList<>();

    public RecentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_recents, container, false);

        ivNoRecents = view.findViewById(R.id.ivNoRecents);
        tvNoRecents=view.findViewById(R.id.tvNoRecents);
        recentsRecyclerView = view.findViewById(R.id.recents_recycler_view);
        recentShimmer=view.findViewById(R.id.recent_shimmer);

        linearLayoutManager=new LinearLayoutManager(getContext());
        recentsRecyclerView.setLayoutManager(linearLayoutManager);
        recentsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        recentPresenter = new RecentsListPresenter();

        recentPresenter.attach(this);

        new Thread(() -> recentPresenter.fetchConversations(getContext())).start();



        recentsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recentsRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View var1, int position) {
//                if (var1.getTag(R.string.user) instanceof User) {
//                    String recentID = (String) var1.getTag(R.string.contact_id);
//                    String recentName = (String) var1.getTag(R.string.contact_name);
//                    String userAvatar = (String) var1.getTag(R.string.user_avatar);
//                    User user = (User) var1.getTag(R.string.user);
//                    RecentsListAdapter.RecentsViewHolder recentViewHolder = (RecentsListAdapter.RecentsViewHolder) var1.getTag(R.string.userHolder);
//                    Log.e("onUserClick: ", recentID+"-"+recentName);
//                    Pair<View, String> p1 = Pair.create(((View) recentViewHolder.avatar), "profilePic");
//                    Pair<View, String> p2 = Pair.create(((View) recentViewHolder.userName), "Name");
//                    Pair<View, String> p3 = Pair.create(((View) recentViewHolder.lastMessage), "lastMessage");
//                    ((View) recentViewHolder.unreadCount).setVisibility(View.GONE);
//                    Intent intent = new Intent(getContext(), OneToOneChatActivity.class);
//                    intent.putExtra(StringContract.IntentStrings.USER_ID, recentID);
//                    intent.putExtra(StringContract.IntentStrings.USER_AVATAR, userAvatar);
//                    intent.putExtra(StringContract.IntentStrings.USER_NAME, recentName);
//                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2, p3);
//                    startActivity(intent, optionsCompat.toBundle());
//                } else
//                {
//                    String recentID = (String) var1.getTag(R.string.group_id);
//                    String recentName = (String) var1.getTag(R.string.group_name);
//                    String userAvatar = (String) var1.getTag(R.string.group_icon);

//                    Log.e("onGroupClick: ", recentID+"-"+recentName);
//                    Group user = (Group) var1.getTag(R.string.group);
                    RecentsListAdapter.RecentsViewHolder recentViewHolder = (RecentsListAdapter.RecentsViewHolder) var1.getTag(R.string.groupHolder);
//                    ((View) recentViewHolder.unreadCount).setVisibility(View.GONE);
//                    Intent intent = new Intent(getContext(), GroupChatActivity.class);
//                    intent.putExtra(StringContract.IntentStrings.INTENT_GROUP_ID, recentID);
//                    intent.putExtra(StringContract.IntentStrings.INTENT_GROUP_NAME, recentName);
//                    intent.putExtra()
//                    startActivity(intent);
//                }
            }

            @Override
            public void onLongClick(View var1, int position) {
            }
        }));

             recentsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                 @Override
                 public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                     if (!recyclerView.canScrollVertically(1)) {
                         recentPresenter.fetchConversations(getContext());
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
    public void setFilterList(List<Conversation> hashMap) {
        recentsListAdapter.setFilterList(hashMap);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recentPresenter.removeMessageListener(getString(R.string.presenceListener));
    }

    @Override
    public void onStart() {
        super.onStart();
        recentPresenter.addMessageListener(getString(R.string.presenceListener));
    }

    @Override
    public void onResume() {
        super.onResume();
        recentPresenter.addMessageListener(getString(R.string.presenceListener));
        recentPresenter.fetchConversations(getContext());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        scrollHelper = (ScrollHelper) context;

    }

    @Override
    public void refreshConversation(BaseMessage message) {
        Conversation newConversation= CometChatHelper.getConversationFromMessage(message);
         recentsListAdapter.updateConversation(newConversation);
    }
    @Override
    public void setRecentAdapter(List<Conversation> conversationList) {
        this.conversationList.addAll(conversationList);
        Log.e( "setRecentAdapter: ",conversationList.toString() );
        if (recentsListAdapter == null) {
            recentsListAdapter = new RecentsListAdapter(conversationList, getContext(), R.layout.recent_list_item,false);
            recentsRecyclerView.setAdapter(recentsListAdapter);
            recentShimmer.stopShimmer();
            recentShimmer.setVisibility(View.GONE);

        } else {
            if (this.conversationList != null) {
                recentsListAdapter.refreshData(conversationList);
            }
        }

        if (this.conversationList != null && this.conversationList.size() == 0) {
            tvNoRecents.setVisibility(View.VISIBLE);
            ivNoRecents.setVisibility(View.VISIBLE);
        } else {
            tvNoRecents.setVisibility(View.GONE);
            ivNoRecents.setVisibility(View.GONE);
        }

    }

    @Override
    public void updateUnreadCount(Conversation conversation) {

    }

    @Override
    public void setLastMessage(Conversation conversation) {

    }


    public void search(String s) {
        recentPresenter.searchUser(s);
    }
}
