package com.autostrad.rentcar.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityProfileViewBinding;
import com.autostrad.rentcar.fragment.AdditionalDriverDocumentFragment;
import com.autostrad.rentcar.fragment.AdditionalDriverFragment;
import com.autostrad.rentcar.fragment.BookingFragment;
import com.autostrad.rentcar.fragment.DocumentFragment;
import com.autostrad.rentcar.fragment.FastLoyaltyFragment;
import com.autostrad.rentcar.fragment.InvoiceFragment;
import com.autostrad.rentcar.fragment.ManagePaymentFragment;
import com.autostrad.rentcar.fragment.MyAccountFragment;
import com.autostrad.rentcar.fragment.RefundFragment;
import com.autostrad.rentcar.fragment.SalikChargesFragment;
import com.autostrad.rentcar.fragment.TrafficLinesFragment;
import com.autostrad.rentcar.model.ImagePathModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.PdfDownloader;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.WindowView;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private LoadingDialog loadingDialog;
    private Session session;

    private static final int READ_WRITE = 20;
    private static final int PDF_DATA = 22;
    String pdf_url;
    String pdf_title;
    String documentName;
    String base64Image;

    public int download = 1;
    public int upload = 2;
    public int click = 0;

    TextView textViewDocumnt;
    TextView txtImagePath;

    public static List<ImagePathModel> imagePathModelList;

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

        getAccess();

        loadingDialog = new LoadingDialog(activity);
        session = new Session(activity);

        imagePathModelList = new ArrayList<>();

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

    public void uploadDocument(String name, TextView textView, TextView txtImage) {
        textViewDocumnt = textView;
        txtImagePath = txtImage;
        documentName = name;
        click = upload;
        getPermission();

    }

    private void getAccess() {
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        } catch (Exception e) {
        }
    }

    public void checkPDF(String path) {
        click = download;
        pdf_url = path;
        pdf_title = randomText();
        if (TextUtils.isEmpty(pdf_url) || pdf_url.equals("null")) {
            H.showMessage(activity, getResources().getString(R.string.noPdfFound));
        } else {
            getPermission();
        }
    }

    private void getPermission() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                READ_WRITE);
    }

    public void jumpToSetting() {
        H.showMessage(activity, getResources().getString(R.string.allowPermission));
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_WRITE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (click==upload){
                        getDocument();
                    }else if (click==download){
                        checkDirectory(activity, pdf_url, pdf_title);
                    }
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    jumpToSetting();
                } else {
                    getPermission();
                }
                return;
            }

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PDF_DATA:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    try {
                        Uri selectedPDF = data.getData();

                        String path = getBase64Image(selectedPDF);
                        hitUploadImage(path);

                    } catch (Exception e) {
                        H.showMessage(activity,e.getMessage());
                    }
                }
                break;
        }
    }


    private void getDocument(){
        try {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, PDF_DATA);
        } catch (Exception e) {
        }
    }

    private void checkDirectory(Context context, String fileURL, String title) {
        try {
            String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FastUAE/Pdf/";
            String fileName = title + ".pdf";
            destination += fileName;
            File direct = new File(destination);
            if (direct.exists()) {
                openPdf(context, destination);
            } else {
                PdfDownloader.download(context, fileURL, title, Config.OPEN);
            }
        } catch (Exception e) {
            H.showMessage(context, getResources().getString(R.string.somethingWrong));
        }

    }

    private void openPdf(Context context, String filepath) {
        File pdfFile = new File(filepath);
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            context.startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            H.showMessage(context, getResources().getString(R.string.noPdfAppAvailable));
        }
    }

    private String randomText() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


    private String getBase64Image(Uri uri) {
        base64Image = "";
        try {
            InputStream imageStream = getContentResolver().openInputStream(uri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            base64Image = encodeImage(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("TAG", "setImageDataEE: "+ e.getMessage() );
            H.showMessage(activity, "Unable to get image, try again.");
        }

        return base64Image;
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }


    private void hitUploadImage(String base64Image) {

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.image,base64Image);
        j.addString(P.extension,"png");
        Api.newApi(activity, P.BaseUrl + "upload_image").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    ProgressView.dismiss(loadingDialog);
                    if (json.getInt(P.status) == 1) {
                        json = json.getJson(P.data);
                        String image = json.getString(P.image);
                        String image_url = json.getString(P.image_url);
                        ImagePathModel model = new ImagePathModel();
                        model.setPath(image);
                        model.setTitle(documentName);
                        imagePathModelList.add(model);
                        textViewDocumnt.setText(getResources().getString(R.string.uploaded) + " " +documentName);
                        txtImagePath.setText(getResources().getString(R.string.imagePath) + " " +image);
                        txtImagePath.setTag(image);
                        txtImagePath.setVisibility(View.VISIBLE);
                        txtImagePath.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewDialog(image_url);
                            }
                        });
                        H.showMessage(activity,getResources().getString(R.string.imageUploaded));
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                })
                .run("hitUploadImage",session.getString(P.token));

    }

    private void documentDialog(String imagePath) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_document_view);

        ImageView imgDocument = dialog.findViewById(R.id.imgDocument);
        LoadImage.glideString(activity, imgDocument, imagePath);

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private void viewDialog(String imagePath) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_image_view);

        PhotoView imageView = dialog.findViewById(R.id.imageView);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);

        LoadImage.glideString(activity,imageView,imagePath);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imagePathModelList!=null){
            imagePathModelList.clear();
        }
    }

    @Override
    public void onBackPressed() {
        if (AdditionalDriverDocumentFragment.forAddData){
            additionalDriverDocumentFragment.callBackAdd();
        }else if (AdditionalDriverFragment.forEditData){
            additionalDriverFragment.callBackEdit();
        }else if (AdditionalDriverFragment.forAddData){
            additionalDriverFragment.callBackAdd();
        }else {
            finish();
        }

    }
}