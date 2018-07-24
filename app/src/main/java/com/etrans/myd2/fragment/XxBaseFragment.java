package com.etrans.myd2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etrans.myd2.R;
import com.etrans.myd2.entity.BaseDayVehicleState;


public class XxBaseFragment extends Fragment
{
  private boolean isShowDoor;
  protected boolean isStart;
  private ImageView ivCarState;
  protected View view;
  private int layoutId;
  private TextView tvAlarmMsg;
  private ImageView iv_car_alert;

  public XxBaseFragment(int paramInt, boolean paramBoolean) {
    layoutId = paramInt;
    isShowDoor = paramBoolean;
  }

  private void setView() {
    if (isShowDoor) {
      ivCarState = ((ImageView)view.findViewById(R.id.iv_car_door_state));
      tvAlarmMsg = (TextView) view.findViewById(R.id.tv_car_state_text);
      iv_car_alert = (ImageView) view.findViewById(R.id.iv_car_alert);

    }
  }

   public boolean isStart() {
    return isStart;
  }

  public void onActivityCreated(Bundle paramBundle) {
    super.onActivityCreated(paramBundle);
    setView();
  }

  @Override
  public void onStart() {
    super.onStart();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    View localView = paramLayoutInflater.inflate(layoutId, null);
    view = localView;
    return localView;
  }

  public void onDestroyView() {
    isStart = false;
    super.onDestroyView();
  }

  public void refreshBaseData(BaseDayVehicleState paramBaseDayVehicleState, int[] paramArrayOfInt) {
    String str1;
    String str2;
    String str3;
    int j;
    if (paramBaseDayVehicleState != null) {
      str1 = paramBaseDayVehicleState.getCarLeftDoor();
      str2 = paramBaseDayVehicleState.getCarRightDoor();
      str3 = paramBaseDayVehicleState.getBehindDoor();
      if (("0".equals(str1)) && ("0".equals(str2)) && ("0".equals(str3))) {
        j = 0;
        ivCarState.setImageResource(paramArrayOfInt[j]);
        iv_car_alert.setVisibility(View.INVISIBLE);
        tvAlarmMsg.setVisibility(View.INVISIBLE);

        return;
      }
      if (("1".equals(str1)) && ("1".equals(str2)) && ("1".equals(str3))) {
        j = 1;
        ivCarState.setImageResource(paramArrayOfInt[j]);
        iv_car_alert.setVisibility(View.VISIBLE);
        tvAlarmMsg.setVisibility(View.VISIBLE);
        tvAlarmMsg.setText("门全部开");
        return;
      }
      if (("1".equals(str1)) && ("1".equals(str2)) && ("0".equals(str3))) {
        j = 2;
        ivCarState.setImageResource(paramArrayOfInt[j]);
        iv_car_alert.setVisibility(View.VISIBLE);
        tvAlarmMsg.setVisibility(View.VISIBLE);
        tvAlarmMsg.setText("左右门开");
        return;
      }
      if (("1".equals(str1)) && ("0".equals(str2)) && ("0".equals(str3))) {
        j = 3;
        ivCarState.setImageResource(paramArrayOfInt[j]);
        iv_car_alert.setVisibility(View.VISIBLE);
        tvAlarmMsg.setVisibility(View.VISIBLE);
        tvAlarmMsg.setText("左门开");
        return;
      }
      if (("0".equals(str1)) && ("1".equals(str2)) && ("0".equals(str3))) {
        j = 4;
        ivCarState.setImageResource(paramArrayOfInt[j]);
        iv_car_alert.setVisibility(View.VISIBLE);
        tvAlarmMsg.setVisibility(View.VISIBLE);
        tvAlarmMsg.setText("右门开");
        return;
      }
      if (("0".equals(str1) && ("0".equals(str2)) && ("1".equals(str3)))) {
        j = 5;
        ivCarState.setImageResource(paramArrayOfInt[j]);
        iv_car_alert.setVisibility(View.VISIBLE);
        tvAlarmMsg.setVisibility(View.VISIBLE);
        tvAlarmMsg.setText("后门开");
        return;
      }
    }
  }
}
