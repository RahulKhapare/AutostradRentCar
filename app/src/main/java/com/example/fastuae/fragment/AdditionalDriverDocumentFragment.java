package com.example.fastuae.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.activity.ProfileViewActivity;
import com.example.fastuae.adapter.DocumentFilterAdapter;
import com.example.fastuae.adapter.LocationFilterAdapter;
import com.example.fastuae.adapter.PendingDocumentAdapter;
import com.example.fastuae.adapter.UploadedDocumentAdapter;
import com.example.fastuae.databinding.FragmentAdditionalDriveDocumentBinding;
import com.example.fastuae.model.DocumentFilterModel;
import com.example.fastuae.model.DocumentModel;
import com.example.fastuae.model.FieldModel;
import com.example.fastuae.model.ImagePathModel;
import com.example.fastuae.model.PendingDocumentModel;
import com.example.fastuae.model.UploadedDocumentModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdditionalDriverDocumentFragment extends Fragment implements DocumentFilterAdapter.onClick,PendingDocumentAdapter.onClick{

    private Context context;
    private FragmentAdditionalDriveDocumentBinding binding;
    private String driver_id;

    private Session session;
    private LoadingDialog loadingDialog;
    private String flag;

    private List<DocumentFilterModel> documentFilterModelList;
    private DocumentFilterAdapter documentFilterAdapter;

    private List<UploadedDocumentModel> uploadedDocumentModelList;
    private UploadedDocumentAdapter uploadedDocumentAdapter;
    private List<PendingDocumentModel> pendingDocumentModelList;
    private PendingDocumentAdapter pendingDocumentAdapter;

    Json jsonMain;
    Json jsonChild;
    boolean updateFlag = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_additional_drive_document, container, false);
            context = inflater.getContext();
            initView();
        }

        return binding.getRoot();
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

    private void initView(){

        driver_id = Config.driverIDFORDOC;

        loadingDialog = new LoadingDialog(context);
        session = new Session(context);

        flag = session.getString(P.languageFlag);

        documentFilterModelList = new ArrayList<>();
        documentFilterAdapter = new DocumentFilterAdapter(context,documentFilterModelList,AdditionalDriverDocumentFragment.this,true);
        binding.recyclerDocFilter.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerDocFilter.setNestedScrollingEnabled(false);
        binding.recyclerDocFilter.setAdapter(documentFilterAdapter);

        uploadedDocumentModelList = new ArrayList<>();
        uploadedDocumentAdapter = new UploadedDocumentAdapter(context,uploadedDocumentModelList,AdditionalDriverDocumentFragment.this);

        pendingDocumentModelList = new ArrayList<>();
        pendingDocumentAdapter = new PendingDocumentAdapter(context,pendingDocumentModelList,AdditionalDriverDocumentFragment.this);
        binding.recyclerPendingDocument.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerPendingDocument.setHasFixedSize(true);
        binding.recyclerPendingDocument.setNestedScrollingEnabled(false);
        binding.recyclerPendingDocument.setAdapter(pendingDocumentAdapter);

        checkView();
        onClick();
        hitDocumentData(driver_id);

    }

    private void onClick(){
        binding.cardLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkView();
            }
        });

        binding.txtUploadDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                binding.lnrUploadedDetailsView.setVisibility(View.GONE);
                binding.lnrUploadingListView.setVisibility(View.VISIBLE);
            }
        });

        binding.txtSaveDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkDocument();
            }
        });
    }

    private void checkView() {
        if (binding.lnrDocListView.getVisibility() == View.VISIBLE) {
            binding.lnrDocListView.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgAreaLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgAreaRight.setImageResource(R.drawable.ic_down_arrow);
            }

        } else if (binding.lnrDocListView.getVisibility() == View.GONE) {
            binding.lnrDocListView.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgAreaLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgAreaRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    @Override
    public void onFilterClick(DocumentFilterModel model) {
        checkView();
        binding.txtArea.setText(model.getTitle());
    }

    private void hitDocumentData(String driver_id) {

        ProgressView.show(context,loadingDialog);
        Json j = new Json();
        j.addString(P.driver_id,driver_id);

        Api.newApi(context, P.BaseUrl + "driver_document_list").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        uploadedDocumentModelList.clear();
                        pendingDocumentModelList.clear();

                        uploadedDocumentAdapter.notifyDataSetChanged();
                        pendingDocumentAdapter.notifyDataSetChanged();

                        Json data = json.getJson(P.data);
                        Json document = data.getJson(P.document_list);

                        JsonList uploaded_document = document.getJsonList(P.uploaded_document);
                        JsonList pending_document = document.getJsonList(P.pending_document);
                        JsonList all_document = document.getJsonList(P.all_document);
                        JsonList filter_document_document = document.getJsonList(P.filter_document_document);

                        if (filter_document_document!=null && filter_document_document.size()!=0){
                            for (Json jsonValue : filter_document_document){
                                DocumentFilterModel model = new DocumentFilterModel();
                                model.setTitle(jsonValue.getString("title"));
                                model.setDocument_key(jsonValue.getJsonArray("document_key"));
                                model.setField_for(jsonValue.getJsonArray("field_for"));
                                documentFilterModelList.add(model);
                            }
                            documentFilterAdapter.notifyDataSetChanged();
                        }

                        if (documentFilterModelList.isEmpty()){
                            binding.lnrFilterView.setVisibility(View.GONE);
                        }else {
                            binding.lnrFilterView.setVisibility(View.VISIBLE);
                        }

                        if (uploaded_document!=null && uploaded_document.size()!=0){
                            for (Json jsonValue : uploaded_document){
                                UploadedDocumentModel model = new UploadedDocumentModel();

                                model.setId(jsonValue.getString("id"));
                                model.setUser_id(jsonValue.getString("user_id"));
                                model.setDocument_key(jsonValue.getString("document_key"));
                                model.setLicense_number(jsonValue.getString("license_number"));
                                model.setIssue_date(jsonValue.getString("issue_date"));
                                model.setImage(jsonValue.getString("image"));
                                model.setPassport_number(jsonValue.getString("passport_number"));
                                model.setVisa_issue_date(jsonValue.getString("visa_issue_date"));
                                model.setCredit_card_number(jsonValue.getString("credit_card_number"));
                                model.setId_number(jsonValue.getString("id_number"));
                                model.setEntry_stamp(jsonValue.getString("entry_stamp"));
                                model.setStatus(jsonValue.getString("status"));
                                model.setDelete_flag(jsonValue.getString("delete_flag"));
                                model.setIs_approved(jsonValue.getString("is_approved"));
                                model.setAdd_date(jsonValue.getString("add_date"));
                                model.setData_field_status(jsonValue.getString("data_field_status"));
                                model.setUpdate_date(jsonValue.getString("update_date"));
                                model.setImage_url(jsonValue.getString("image_url"));
                                model.setKey(jsonValue.getString("key"));
                                model.setTitle(jsonValue.getString("title"));
                                model.setField(jsonValue.getJsonArray("field"));
                                model.setField_for(jsonValue.getJsonArray("field_for"));

                                uploadedDocumentModelList.add(model);
                            }
                        }

                        if (pending_document!=null && pending_document.size()!=0){
                            for (Json jsonValue : pending_document){
                                PendingDocumentModel model = new PendingDocumentModel();

                                model.setKey(jsonValue.getString("key"));
                                model.setTitle(jsonValue.getString("title"));
                                model.setField(jsonValue.getJsonArray("field"));
                                model.setField_for(jsonValue.getJsonArray("field_for"));
                                model.setCheckValue("0");
                                try {
                                    model.setSave_data(jsonValue.getJson(P.save_data));
                                } catch (Exception e) {
                                    model.setSave_data(new Json());
                                }
                                pendingDocumentModelList.add(model);
                            }
                            pendingDocumentAdapter.notifyDataSetChanged();
                        }

                        if (uploadedDocumentModelList.isEmpty()){
                            binding.lnrUploadedDetailsView.setVisibility(View.GONE);
                            binding.lnrUploadingListView.setVisibility(View.VISIBLE);
                        }else {
                            binding.lnrUploadedDetailsView.setVisibility(View.VISIBLE);
                            binding.lnrUploadingListView.setVisibility(View.GONE);
                        }

                        checkData();

                    }else {
                        checkData();
                        H.showMessage(context,json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitDocumentData",session.getString(P.token));
    }


    @Override
    public void downloadDocument(String name, TextView textView, TextView txtImagePath) {
        ((ProfileViewActivity)getActivity()).uploadDocument(name,textView,txtImagePath);
    }


    private void checkDocument(){

        updateFlag = true;
        jsonMain = new Json();
        jsonChild = new Json();

        try {
            for (PendingDocumentModel model : pendingDocumentModelList){

                if (model.getCheckValue().equals("1")){
                    jsonMain.addString("driver_id",driver_id);
                    for (FieldModel fieldModel : model.getFieldList()){

                        for (ImagePathModel imageModel : ProfileViewActivity.imagePathModelList){
                            if (imageModel.getTitle().equals(model.getTitle())){
                                if (fieldModel.getJson().has(P.image)){
                                    fieldModel.getJson().remove(P.image);
                                    fieldModel.getJson().addString(P.image,imageModel.getPath());
                                }else {
                                    fieldModel.getJson().addString(P.image,imageModel.getPath());
                                }
                            }
                        }

                        jsonChild.addJSON(model.getKey(),fieldModel.getJson());

//                        if (fieldModel.getJson().has("key")){
//                            fieldModel.getJson().remove("key");
//                        }else {
//                            fieldModel.getJson().addString("key","1");
//                        }

                        fieldModel.getJson().addString("key","1");

                        if (!fieldModel.getJson().has(fieldModel.getFiled())) {
                            String valueKey = fieldModel.getFiled().replace("_", " ");
                            H.showMessage(context, getResources().getString(R.string.enter) + " " + capitalize(valueKey));
                            updateFlag = false;
                            return;
                        }

                        if (!fieldModel.getJson().has("image")) {
                            H.showMessage(context, getResources().getString(R.string.selectImage)+ " " + model.getTitle());
                            updateFlag = false;
                            return;
                        }
                    }
                }else {

                }

                jsonMain.addJSON(P.document,jsonChild);
            }

            if (jsonMain.getJson(P.document).toString().equals("{}")){
                H.showMessage(context, getResources().getString(R.string.checkDocument));
            }else {
                if (updateFlag){
//                    Log.e("TAG", "checkDocumentAASAS: "+ jsonMain.toString() );
                    hitSaveUserDocumentDetails(jsonMain);
                }else {
                    H.showMessage(context, getResources().getString(R.string.checkDocument));
                }
            }

        }catch (Exception e){
//            Log.e("TAG", "checkDocument: "+ e.getMessage() );
            H.showMessage(context, getResources().getString(R.string.somethingWrong));
        }
    }

    private void hitSaveUserDocumentDetails(Json j) {

        ProgressView.show(context,loadingDialog);
        Api.newApi(context, P.BaseUrl + "driver_save_documents").addJson(j)
                .setMethod(Api.POST)
//                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        if (ProfileViewActivity.imagePathModelList!=null){
                            ProfileViewActivity.imagePathModelList.clear();
                        }
                        hitDocumentData(driver_id);
                        H.showMessage(context,getResources().getString(R.string.dataUpdated));
                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitSaveUserDocumentDetails",session.getString(P.token));

    }

    private void checkData(){
        if (pendingDocumentModelList.isEmpty()){
            binding.txtSaveDocument.setVisibility(View.GONE);
        }else {
            binding.txtSaveDocument.setVisibility(View.VISIBLE);
        }
    }


    private String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    public static AdditionalDriverDocumentFragment newInstance() {
        AdditionalDriverDocumentFragment fragment = new AdditionalDriverDocumentFragment();
        return fragment;
    }
}
