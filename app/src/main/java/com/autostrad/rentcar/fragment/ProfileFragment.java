package com.autostrad.rentcar.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.LoginDashboardActivity;
import com.autostrad.rentcar.activity.ProfileViewActivity;
import com.autostrad.rentcar.databinding.FragmentProfileBinding;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.P;

public class ProfileFragment extends Fragment{

    private Context context;
    private FragmentProfileBinding binding;
    private Session session;
    private String flag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
            context = inflater.getContext();

            initView();
        }

        return binding.getRoot();
    }



    private void initView() {

        session = new Session(context);
        flag = session.getString(P.languageFlag);

        onClick();
        updateIcons();
    }

    @Override
    public void onDestroyView()
    {
        if (binding.getRoot() != null)
        {
            ViewGroup parentViewGroup = (ViewGroup) binding.getRoot().getParent();

            if (parentViewGroup != null)
            {
                parentViewGroup.removeAllViews();
            }
        }

        super.onDestroyView();
    }


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    private void onClick(){

        binding.lnrMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Config.currentProfileFlag = Config.My_Account;
                jumpToProfile();

            }
        });

        binding.lnrDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Config.currentProfileFlag = Config.Documents;
                jumpToProfile();
            }
        });

        binding.lnrAdditionalDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Config.currentProfileFlag = Config.Additional_Driver;
                jumpToProfile();
            }
        });

        binding.lnrAdditionalDriveDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Config.currentProfileFlag = Config.Additional_Driver_Document;
                jumpToProfile();
            }
        });

        binding.lnrBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Config.currentProfileFlag = Config.Booking;
                jumpToProfile();
            }
        });

        binding.lnrPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Config.currentProfileFlag = Config.Manage_Payments;
                jumpToProfile();
            }
        });

        binding.lnrCharges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Config.currentProfileFlag = Config.Invoices;
                jumpToProfile();

            }
        });

        binding.lnrFastLoyalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Config.currentProfileFlag = Config.Fast_Loyalty;
                jumpToProfile();
            }
        });

        binding.lnrRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Config.currentProfileFlag = Config.Refund;
                jumpToProfile();
            }
        });

        binding.lnrLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                onLogoutClick();
            }
        });

    }

    private void jumpToProfile(){
        Intent intent = new Intent(context, ProfileViewActivity.class);
        startActivity(intent);
    }

    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgMyAccountRight.setVisibility(View.GONE);
            binding.imgMyAccountLeft.setVisibility(View.VISIBLE);

            binding.imgDocumentRight.setVisibility(View.GONE);
            binding.imgDocumentLeft.setVisibility(View.VISIBLE);

            binding.imgAdditionalDriveRight.setVisibility(View.GONE);
            binding.imgAdditionalDriveLeft.setVisibility(View.VISIBLE);

            binding.imgAdditionalDriveDocumentRight.setVisibility(View.GONE);
            binding.imgAdditionalDriveDocumentLeft.setVisibility(View.VISIBLE);

            binding.imgBookingRight.setVisibility(View.GONE);
            binding.imgBookingLeft.setVisibility(View.VISIBLE);

            binding.imgPaymentRight.setVisibility(View.GONE);
            binding.imgPaymentLeft.setVisibility(View.VISIBLE);

            binding.imgChargesRight.setVisibility(View.GONE);
            binding.imgChargesLeft.setVisibility(View.VISIBLE);

            binding.imgFastLoyaltyRight.setVisibility(View.GONE);
            binding.imgFastLoyaltyLeft.setVisibility(View.VISIBLE);

            binding.imgRefundRight.setVisibility(View.GONE);
            binding.imgRefundLeft.setVisibility(View.VISIBLE);

        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgMyAccountRight.setVisibility(View.VISIBLE);
            binding.imgMyAccountLeft.setVisibility(View.GONE);

            binding.imgDocumentRight.setVisibility(View.VISIBLE);
            binding.imgDocumentLeft.setVisibility(View.GONE);

            binding.imgAdditionalDriveRight.setVisibility(View.VISIBLE);
            binding.imgAdditionalDriveLeft.setVisibility(View.GONE);

            binding.imgAdditionalDriveDocumentRight.setVisibility(View.VISIBLE);
            binding.imgAdditionalDriveDocumentLeft.setVisibility(View.GONE);

            binding.imgBookingRight.setVisibility(View.VISIBLE);
            binding.imgBookingLeft.setVisibility(View.GONE);

            binding.imgPaymentRight.setVisibility(View.VISIBLE);
            binding.imgPaymentLeft.setVisibility(View.GONE);

            binding.imgChargesRight.setVisibility(View.VISIBLE);
            binding.imgChargesLeft.setVisibility(View.GONE);

            binding.imgFastLoyaltyRight.setVisibility(View.VISIBLE);
            binding.imgFastLoyaltyLeft.setVisibility(View.GONE);

            binding.imgRefundRight.setVisibility(View.VISIBLE);
            binding.imgRefundLeft.setVisibility(View.GONE);

        }
    }

    private void onLogoutClick() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        alertDialogBuilder.setTitle(getResources().getString(R.string.logOut));
        alertDialogBuilder.setMessage(getResources().getString(R.string.logOutMessage));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                        session.clear();
                        new Session(getActivity()).addString(P.languageFlag, Config.ENGLISH);
                        Intent intent = new Intent(context, LoginDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


}
