package com.example.fastuae.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityProfileViewBinding;
import com.example.fastuae.fragment.AdditionalDriverDocumentFragment;
import com.example.fastuae.fragment.AdditionalDriverFragment;
import com.example.fastuae.fragment.BookingFragment;
import com.example.fastuae.fragment.DocumentFragment;
import com.example.fastuae.fragment.FastLoyaltyFragment;
import com.example.fastuae.fragment.InvoiceFragment;
import com.example.fastuae.fragment.ManagePaymentFragment;
import com.example.fastuae.fragment.MyAccountFragment;
import com.example.fastuae.fragment.RefundFragment;
import com.example.fastuae.fragment.SalikChargesFragment;
import com.example.fastuae.fragment.TrafficLinesFragment;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.WindowView;

public class ProfileViewActivity extends AppCompatActivity{

    private ProfileViewActivity activity = this;
    private ActivityProfileViewBinding binding;

    private MyAccountFragment myAccountFragment;
    private DocumentFragment documentFragment;
    private AdditionalDriverFragment additionalDriverFragment;
    private AdditionalDriverDocumentFragment additionalDriverDocumentFragment;
    private BookingFragment bookingFragment;
    private ManagePaymentFragment managePaymentFragment;
    private InvoiceFragment invoiceFragment;
    private SalikChargesFragment salikChargesFragment;
    private TrafficLinesFragment trafficLinesFragment;
    private FastLoyaltyFragment fastLoyaltyFragment;
    private RefundFragment refundFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_view);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.profile));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        loadFragment();

    }

    private void loadFragment(){
        String categoryFlag = Config.currentProfileFlag;
        if (categoryFlag.equals(Config.My_Account)) {
            binding.txtTitle.setText(getResources().getString(R.string.myAccount));
            myAccountFragment = MyAccountFragment.newInstance();
            fragmentLoader(myAccountFragment, Config.My_Account);
        } else if (categoryFlag.equals(Config.Documents)) {
            binding.txtTitle.setText(getResources().getString(R.string.document));
            documentFragment = DocumentFragment.newInstance();
            fragmentLoader(documentFragment, Config.Documents);
        } else if (categoryFlag.equals(Config.Additional_Driver)) {
            binding.txtTitle.setText(getResources().getString(R.string.additionalDriver));
            additionalDriverFragment = AdditionalDriverFragment.newInstance();
            fragmentLoader(additionalDriverFragment, Config.Additional_Driver);
        } else if (categoryFlag.equals(Config.Additional_Driver_Document)) {
            binding.txtTitle.setText(getResources().getString(R.string.additionLaDriveDocument));
            additionalDriverDocumentFragment = AdditionalDriverDocumentFragment.newInstance();
            fragmentLoader(additionalDriverDocumentFragment, Config.Additional_Driver_Document);
        } else if (categoryFlag.equals(Config.Booking)) {
            binding.txtTitle.setText(getResources().getString(R.string.booking));
            bookingFragment = BookingFragment.newInstance();
            fragmentLoader(bookingFragment, Config.Booking);
        } else if (categoryFlag.equals(Config.Manage_Payments)) {
            binding.txtTitle.setText(getResources().getString(R.string.yourPayment));
            managePaymentFragment = ManagePaymentFragment.newInstance();
            fragmentLoader(managePaymentFragment, Config.Manage_Payments);
        } else if (categoryFlag.equals(Config.Invoices)) {
            binding.txtTitle.setText(getResources().getString(R.string.chargesFine));
            invoiceFragment = InvoiceFragment.newInstance();
            fragmentLoader(invoiceFragment, Config.Invoices);
        } else if (categoryFlag.equals(Config.Salik_Charges)) {
            binding.txtTitle.setText(getResources().getString(R.string.salik_Charges));
            salikChargesFragment = SalikChargesFragment.newInstance();
            fragmentLoader(salikChargesFragment, Config.Salik_Charges);
        } else if (categoryFlag.equals(Config.Traffic_Lines)) {
            binding.txtTitle.setText(getResources().getString(R.string.traffic_Lines));
            trafficLinesFragment = TrafficLinesFragment.newInstance();
            fragmentLoader(trafficLinesFragment, Config.Traffic_Lines);
        }else if (categoryFlag.equals(Config.Fast_Loyalty)) {
            binding.txtTitle.setText(getResources().getString(R.string.fastLoyalty));
            fastLoyaltyFragment = FastLoyaltyFragment.newInstance();
            fragmentLoader(fastLoyaltyFragment, Config.Fast_Loyalty);
        }else if (categoryFlag.equals(Config.Refund)) {
            binding.txtTitle.setText(getResources().getString(R.string.refund));
            refundFragment = RefundFragment.newInstance();
            fragmentLoader(refundFragment, Config.Refund);
        }
    }

    public void fragmentLoader(Fragment fragment, String tag) {
        Config.currentProfileFlag = tag;
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit)
                .replace(R.id.fragmentFrame, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}