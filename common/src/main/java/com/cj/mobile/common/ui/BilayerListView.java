package com.cj.mobile.common.ui;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cj.mobile.common.R;
import com.cj.mobile.common.model.BilayerShowInfo;
import com.cj.mobile.common.model.GroupInfo;

/**
 * 双层列表(描述：例如 QQ列表，以ExpandableListView控件为基础的一个扩展)
 * @author 王力杨
 *
 */
public class BilayerListView extends ExpandableListView {
	public BilayerListListener bilayerListListener = null;
	public interface BilayerListListener {
		public void onOpenContent(String parentId,String parentName,String Id,String title);
	}
	
	/**数据*/
	private List<GroupInfo> groupList = new ArrayList<GroupInfo>();
	private Activity activity;
	
	public BilayerListView(Context context) {
		super(context);
	}

	public BilayerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BilayerListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 * 初始化 控件
	 */
	public void init(Activity activity,List<GroupInfo> groupList) {
		this.activity = activity;
		this.groupList = groupList;
	}
	
	/**
	 * 加载数据
	 * @param shieldingEvent	屏蔽事件(组不可点击)
	 * @param isAllOpen			是否全部展开
	 * @param defaultOpen		如果全部展开=false,可选择默认展开第一项(下标)
	 */
	public void loadData(final boolean shieldingEvent,boolean isAllOpen,int defaultOpen){
		if(this.groupList == null || this.groupList.size() == 0)
			return;
		//----------加载数据
		FriendListAdapter adapter = new FriendListAdapter(activity);
		this.setAdapter(adapter);
		
		//----------是否全部展开
		if(isAllOpen){
			if(groupList != null)
			for (int i = 0; i < groupList.size(); i++) {
				this.expandGroup(i);
			}
		}else{
			//----------设置默认展开项
			if(defaultOpen > -1){
				this.expandGroup(defaultOpen);
			}
		}
		
		setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				if(shieldingEvent)
					return true;
				else
					return false;	
			}
		});
		setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

				if(groupList != null && groupList.size() > 0){
					GroupInfo group = groupList.get(groupPosition);
					BilayerShowInfo info = group.getFriendInfoList().get(childPosition);
					if (bilayerListListener != null)
						bilayerListListener.onOpenContent(group.getGroupID(),group.getGroupName(),info.getId(),info.getTitle());
				}

				return false;
			}
		});
		setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return false;
			}
		});

		setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return false;
			}
		});
	}
	
	/**
	 * 适配器，填充好友列表
	 * 
	 * @author 王力杨
	 * 
	 */
	public class FriendListAdapter extends BaseExpandableListAdapter {
		Context context;
		LayoutInflater mChildInflater;

		public FriendListAdapter(Context context) {
			this.mChildInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		class FriendHolder {
			//------分组
			TextView name;
			ImageView iv;
			//------child
			ImageView state;
			ImageView unread;
			
		}

		@Override
		public int getGroupCount() {
			return groupList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			if (groupList.size() > 0) {
				return groupList.get(groupPosition).getFriendInfoList().size();
			} else {
				return 0;
			}

		}

		@Override
		public GroupInfo getGroup(int groupPosition) {

			return groupList.get(groupPosition);
		}

		public GroupInfo getGroup(String groupName) {
			GroupInfo groupInfo = null;
			if (getGroupCount() > 0) {
				for (int i = 0, j = getGroupCount(); i < j; i++) {
					GroupInfo holder = (GroupInfo) getGroup(i);
					if (TextUtils.isEmpty(holder.getGroupName())) {
						groupList.remove(holder);
					} else {
						if (holder.getGroupName().equals(groupInfo)) {
							groupInfo = holder;
						}
					}
				}
			}
			return groupInfo;
		}

		@Override
		public BilayerShowInfo getChild(int groupPosition, int childPosition) {
			return groupList.get(groupPosition).getFriendInfoList().get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		/**
		 * 填充折叠效果
		 */
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			FriendHolder holder;
			if (convertView == null) {
				holder = new FriendHolder();
				convertView = mChildInflater.inflate(R.layout.friend_group_item, null);
				holder.name = (TextView) convertView.findViewById(R.id.friend_group_list_name);
				holder.iv = (ImageView) convertView.findViewById(R.id.friend_group_list_icon);
				convertView.setTag(holder);
			} else {
				holder = (FriendHolder) convertView.getTag();
			}
			
			//拿到当前需要展示的数据
			GroupInfo group = groupList.get(groupPosition);
			
			//把标题展示出来
			String groupname = group.getGroupName();
			holder.name.setText(groupname);
			
			//验证是否显示图片
			if(group.isIMG()){
				//允许展示图片
				holder.iv.setVisibility(View.VISIBLE);
				
				//验证当前是否“打开”
				if (isExpanded) {
					//未打开
					holder.iv.setBackgroundResource(group.getImgNormal());//R.drawable.sc_group_expand
				} else {
					//打开
					holder.iv.setBackgroundResource(group.getImgPressed());//R.drawable.sc_group_unexpand
				}
			}else{
				//不允许展示图片
				holder.iv.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}

		/**
		 * 填充好友信息
		 */
		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			FriendHolder holder;
			if (convertView == null) {
				holder = new FriendHolder();
				convertView = mChildInflater.inflate(R.layout.friend_child_item, null);
				holder.name = (TextView) convertView.findViewById(R.id.friend_nickname);
				holder.state = (ImageView) convertView.findViewById(R.id.friend_state);
				holder.unread = (ImageView) convertView.findViewById(R.id.friend_icon);
				convertView.setTag(holder);
			} else {
				holder = (FriendHolder) convertView.getTag();
			}
			//----------拿到当前要展示的数据
			BilayerShowInfo groupname = groupList.get(groupPosition).getFriendInfoList().get(childPosition);
			
			//先展示出标题
			holder.name.setText(groupname.getTitle());
			//验证是否显示右侧图片
			if(groupname.isShowRightImg()){
				//允许展示图片
				holder.state.setVisibility(View.VISIBLE);
				holder.state.setBackgroundResource(groupname.getImgRight());
			}else{
				//不允许展示图片
				holder.state.setVisibility(View.INVISIBLE);
			}
			
			//验证是否显示左侧图片
			if(groupname.isShowLeftImg()){
				//允许展示图片
				holder.unread.setVisibility(View.VISIBLE);
				holder.unread.setBackgroundResource(groupname.getImgLeft());
			}else{
				//不允许展示图片
				holder.unread.setVisibility(View.INVISIBLE);
			}

			if (isLastChild) {
				BilayerListView.this.setItemChecked(groupPosition, true);
			}
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}
}
