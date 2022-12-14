package com.autostrad.rentcar.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.ProfileViewActivity;
import com.autostrad.rentcar.adapter.DocumentEditAdapter;
import com.autostrad.rentcar.adapter.DocumentFilterAdapter;
import com.autostrad.rentcar.adapter.PendingDocumentAdapter;
import com.autostrad.rentcar.adapter.UploadedDocumentAdapter;
import com.autostrad.rentcar.databinding.FragmentAdditionalDriveDocumentBinding;
import com.autostrad.rentcar.model.DocumentFilterModel;
import com.autostrad.rentcar.model.FieldModel;
import com.autostrad.rentcar.model.ImagePathModel;
import com.autostrad.rentcar.model.PendingDocumentModel;
import com.autostrad.rentcar.model.UploadedDocumentModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdditionalDriverDocumentFragment extends Fragment implements DocumentFilterAdapter.onClick, PendingDocumentAdapter.onClick, UploadedDocumentAdapter.onClick {

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

    public static boolean forAddData = false;
    private boolean upadateData = false;

    private String filterTitle = "";

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
    public void onDestroyView() {
        if (binding.getRoot() != null) {
            ViewGroup parentViewGroup = (ViewGroup) binding.getRoot().getParent();

            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }

        super.onDestroyView();
    }

    private void initView() {

        upadateData = false;
        forAddData = false;

        driver_id = Config.driverIDFORDOC;

        loadingDialog = new LoadingDialog(context);
        session = new Session(context);

        flag = session.getString(P.languageFlag);

        documentFilterModelList = new ArrayList<>();
        documentFilterAdapter = new DocumentFilterAdapter(context, documentFilterModelList, AdditionalDriverDocumentFragment.this, true, filterTitle);
        binding.recyclerDocFilter.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerDocFilter.setNestedScrollingEnabled(false);
        binding.recyclerDocFilter.setAdapter(documentFilterAdapter);

        uploadedDocumentModelList = new ArrayList<>();
        uploadedDocumentAdapter = new UploadedDocumentAdapter(context, uploadedDocumentModelList, AdditionalDriverDocumentFragment.this);
        binding.recyclerUploadedDocument.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerUploadedDocument.setHasFixedSize(true);
        binding.recyclerUploadedDocument.setNestedScrollingEnabled(false);
        binding.recyclerUploadedDocument.setAdapter(uploadedDocumentAdapter);

        pendingDocumentModelList = new ArrayList<>();
        pendingDocumentAdapter = new PendingDocumentAdapter(context, pendingDocumentModelList, AdditionalDriverDocumentFragment.this);
        binding.recyclerPendingDocument.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerPendingDocument.setHasFixedSize(true);
        binding.recyclerPendingDocument.setNestedScrollingEnabled(false);
        binding.recyclerPendingDocument.setAdapter(pendingDocumentAdapter);

        onClick();
        checkView();
        hitDocumentData(driver_id);

    }

    public void callBackAdd() {
        if (forAddData) {
            forAddData = false;
            binding.lnrUploadedDetailsView.setVisibility(View.VISIBLE);
            binding.lnrUploadingListView.setVisibility(View.GONE);
            return;
        }
    }


    private void onClick() {
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
                forAddData = true;
                upadateData = true;
                binding.lnrUploadedDetailsView.setVisibility(View.GONE);
                binding.lnrUploadingListView.setVisibility(View.VISIBLE);
                binding.nestedScroll.fullScroll(View.FOCUS_UP);
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

    private void goneView() {
        binding.lnrDocListView.setVisibility(View.GONE);
        if (flag.equals(Config.ARABIC)) {
            binding.imgAreaLeft.setImageResource(R.drawable.ic_down_arrow);
        } else if (flag.equals(Config.ENGLISH)) {
            binding.imgAreaRight.setImageResource(R.drawable.ic_down_arrow);
        }

    }

    @Override
    public void onFilterClick(DocumentFilterModel model) {
        checkView();
        filterTitle = model.getTitle();
        binding.txtArea.setText(model.getTitle());
        applyFilter(model);
    }

    private void applyFilter(DocumentFilterModel model) {
        try {
            List<UploadedDocumentModel> docList = new ArrayList<>();
            for (int i = 0; i < model.getDocument_key().length(); i++) {
                String key = model.getDocument_key().getString(i);
                for (UploadedDocumentModel uploadModel : uploadedDocumentModelList) {
                    if (key.equals(uploadModel.getKey())) {
                        docList.add(uploadModel);
                    }
                }
                updateDocumentList(docList);
            }
            if (docList.isEmpty()) {
//                H.showMessage(context, getResources().getString(R.string.noDataFound));
                binding.txtErrorData.setVisibility(View.VISIBLE);
            }else {
                binding.txtErrorData.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            H.showMessage(context, getResources().getString(R.string.somethingWrong));
        }

    }

    private void updateDocumentList(List<UploadedDocumentModel> uploadedDocumentModelList) {
        UploadedDocumentAdapter uploadedDocumentAdapter = new UploadedDocumentAdapter(context, uploadedDocumentModelList, AdditionalDriverDocumentFragment.this);
        binding.recyclerUploadedDocument.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerUploadedDocument.setHasFixedSize(true);
        binding.recyclerUploadedDocument.setNestedScrollingEnabled(false);
        binding.recyclerUploadedDocument.setAdapter(uploadedDocumentAdapter);
    }

    @Override
    public void documentUploadedEdit(UploadedDocumentModel model) {
        forAddData = false;
        upadateData = true;
        goneView();
        documentEditDialog(model);
    }

    @Override
    public void documentUploadedDelete(UploadedDocumentModel model) {
        forAddData = false;
        upadateData = true;
        goneView();
        documentDeleteDialog(model, getResources().getString(R.string.deleteDocMessage));
    }

    @Override
    public void documentUploadedView(UploadedDocumentModel model) {
        goneView();
        viewDialog(model.getImage_url());
    }

    private void documentDeleteDialog(UploadedDocumentModel model, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        hitDeleteDocumentDetails(dialog, model);
                    }
                });

        builder1.setNegativeButton(
                getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

        Button positiveButton = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
        Button nigativeButton = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.lightBlue));
        nigativeButton.setTextColor(getResources().getColor(R.color.lightBlue));

    }

    private void documentEditDialog(UploadedDocumentModel model) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_document_edit);

        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtUploadedPath = dialog.findViewById(R.id.txtUploadedPath);
        TextView txtUploadDocument = dialog.findViewById(R.id.txtUploadDocument);
        TextView txtSaveDocument = dialog.findViewById(R.id.txtSaveDocument);
        RecyclerView recyclerExtraFields = dialog.findViewById(R.id.recyclerExtraFields);
        txtTitle.setText(getResources().getString(R.string.update) + " " + checkString(model.getTitle()));

        txtUploadedPath.setText(model.getImage());
        List<FieldModel> fieldList = new ArrayList<>();
        try {
            for (int i = 0; i < model.getField().length(); i++) {
                String value = model.getField().getString(i);
                if (!value.equals("image")) {
                    fieldList.add(new FieldModel(value, new Json()));
                }
            }
        } catch (Exception e) {
        }

        model.setFieldList(fieldList);
        DocumentEditAdapter documentEditAdapter = new DocumentEditAdapter(context, model.getFieldList(), new Json(), model.getSave_data(), 2, model.getJsonAllData(),model.getImage());
        recyclerExtraFields.setLayoutManager(new LinearLayoutManager(context));
        recyclerExtraFields.setHasFixedSize(true);
        recyclerExtraFields.setNestedScrollingEnabled(true);
        recyclerExtraFields.setAdapter(documentEditAdapter);

        txtUploadedPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                viewDialog(model.getImage_url());
            }
        });

        txtUploadDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((ProfileViewActivity) getActivity()).uploadDocument(model.getTitle(), txtUploadDocument, txtUploadedPath);
            }
        });

        txtSaveDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkEditDocument(dialog, model);
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private void hitDeleteDocumentDetails(DialogInterface dialog, UploadedDocumentModel model) {

        ProgressView.show(context, loadingDialog);
        Json j = new Json();
        j.addString(P.driver_id, driver_id);
        j.addString(P.document_key, model.getDocument_key());

        Api.newApi(context, P.BaseUrl + "delete_driver_save_documents").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        dialog.dismiss();
                        hitDocumentData(driver_id);
                    } else {
                        H.showMessage(context, json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitDeleteDocumentDetails", session.getString(P.token));
    }

    private void hitDocumentData(String driver_id) {

        ProgressView.show(context, loadingDialog);
        Json j = new Json();
        j.addString(P.driver_id, driver_id);

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
                        documentFilterModelList.clear();

                        updateDocumentList(uploadedDocumentModelList);
                        pendingDocumentAdapter.notifyDataSetChanged();

                        Json data = json.getJson(P.data);
                        Json document = data.getJson(P.document_list);

                        JsonList uploaded_document = document.getJsonList(P.uploaded_document);
                        JsonList pending_document = document.getJsonList(P.pending_document);
                        JsonList all_document = document.getJsonList(P.all_document);
                        JsonList filter_document_document = document.getJsonList(P.filter_document_document);

                        if (uploaded_document != null && uploaded_document.size() != 0) {
//                            Log.e("TAG", "hitDocumentDataASASAS: "+uploaded_document.toString()  );
                            for (Json jsonValue : uploaded_document) {

                                UploadedDocumentModel model = new UploadedDocumentModel();

                                model.setId(jsonValue.getString("id"));
                                model.setUser_id(jsonValue.getString("user_id"));
                                model.setDocument_key(jsonValue.getString("document_key"));
                                model.setLicense_number(jsonValue.getString("license_number"));
                                model.setIssue_date(jsonValue.getString("issue_date"));
                                model.setExpiry(jsonValue.getString("expiry"));
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
                                model.setJsonAllData(jsonValue);
                                try {
                                    model.setSave_data(jsonValue.getJson(P.save_data));
                                } catch (Exception e) {
                                    model.setSave_data(new Json());
                                }
                                uploadedDocumentModelList.add(model);
                            }

                            uploadedDocumentAdapter.notifyDataSetChanged();
                        }

                        if (pending_document != null && pending_document.size() != 0) {
                            for (Json jsonValue : pending_document) {
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

                        if (pendingDocumentModelList.isEmpty()){
                            binding.txtUploadDocument.setVisibility(View.GONE);
                        }else {
                            binding.txtUploadDocument.setVisibility(View.VISIBLE);
                        }

                        if (uploadedDocumentModelList.isEmpty()) {
                            binding.lnrUploadedDetailsView.setVisibility(View.GONE);
                            binding.lnrUploadingListView.setVisibility(View.VISIBLE);
                        } else {
                            binding.lnrUploadedDetailsView.setVisibility(View.VISIBLE);
                            binding.lnrUploadingListView.setVisibility(View.GONE);
                        }

                        if (filter_document_document != null && filter_document_document.size() != 0) {
                            for (Json jsonValue : filter_document_document) {
                                DocumentFilterModel model = new DocumentFilterModel();
                                model.setTitle(jsonValue.getString("title"));
                                model.setDocument_key(jsonValue.getJsonArray("document_key"));
                                model.setField_for(jsonValue.getJsonArray("field_for"));
                                documentFilterModelList.add(model);
                            }
                            DocumentFilterAdapter documentFilterAdapter = new DocumentFilterAdapter(context, documentFilterModelList, AdditionalDriverDocumentFragment.this, true, filterTitle);
                            binding.recyclerDocFilter.setLayoutManager(new LinearLayoutManager(context));
                            binding.recyclerDocFilter.setNestedScrollingEnabled(false);
                            binding.recyclerDocFilter.setAdapter(documentFilterAdapter);
                            if (upadateData){
                                upadateData = false;
                                binding.cardLocation.performClick();
                            }
                        }

                        if (documentFilterModelList.isEmpty()) {
                            binding.lnrFilterView.setVisibility(View.GONE);
                        } else {
                            binding.lnrFilterView.setVisibility(View.VISIBLE);
                        }


                        checkData();

                        binding.nestedScroll.fullScroll(View.FOCUS_UP);
                    } else {
                        binding.nestedScroll.fullScroll(View.FOCUS_UP);
                        checkData();
                        H.showMessage(context, json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitDocumentData", session.getString(P.token));
    }


    @Override
    public void downloadDocument(String name, TextView textView, TextView txtImagePath) {
        ((ProfileViewActivity) getActivity()).uploadDocument(name, textView, txtImagePath);
    }


    private void checkDocument() {

        updateFlag = true;
        jsonMain = new Json();
        jsonChild = new Json();

        try {
            for (PendingDocumentModel model : pendingDocumentModelList) {

                if (model.getCheckValue().equals("1")) {
                    jsonMain.addString("driver_id", driver_id);
                    for (FieldModel fieldModel : model.getFieldList()) {

                        for (ImagePathModel imageModel : ProfileViewActivity.imagePathModelList) {
                            if (imageModel.getTitle().equals(model.getTitle())) {
                                if (fieldModel.getJson().has(P.image)) {
                                    fieldModel.getJson().remove(P.image);
                                    fieldModel.getJson().addString(P.image, imageModel.getPath());
                                } else {
                                    fieldModel.getJson().addString(P.image, imageModel.getPath());
                                }
                            }
                        }

                        jsonChild.addJSON(model.getKey(), fieldModel.getJson());

//                        if (fieldModel.getJson().has("key")){
//                            fieldModel.getJson().remove("key");
//                        }else {
//                            fieldModel.getJson().addString("key","1");
//                        }

                        fieldModel.getJson().addString("key", "1");

                        if (!fieldModel.getJson().has(fieldModel.getFiled())) {
                            String valueKey = fieldModel.getFiled().replace("_", " ");
                            H.showMessage(context, getResources().getString(R.string.enter) + " " + capitalize(valueKey));
                            updateFlag = false;
                            return;
                        }

                        if (!fieldModel.getJson().has("image")) {
                            H.showMessage(context, getResources().getString(R.string.selectImage) + " " + model.getTitle());
                            updateFlag = false;
                            return;
                        }
                    }
                } else {

                }

                jsonMain.addJSON(P.document, jsonChild);
            }

            if (jsonMain.getJson(P.document).toString().equals("{}")) {
                H.showMessage(context, getResources().getString(R.string.checkDocument));
            } else {
                if (updateFlag) {
//                    Log.e("TAG", "checkDocumentAASAS: "+ jsonMain.toString() );
                    hitSaveUserDocumentDetails(jsonMain);
                } else {
                    H.showMessage(context, getResources().getString(R.string.checkDocument));
                }
            }

        } catch (Exception e) {
//            Log.e("TAG", "checkDocument: "+ e.getMessage() );
            H.showMessage(context, getResources().getString(R.string.somethingWrong));
        }
    }

    private void checkEditDocument(Dialog dialog, UploadedDocumentModel dataModel) {

        updateFlag = true;
        jsonMain = new Json();
        jsonChild = new Json();

        try {
            UploadedDocumentModel model = dataModel;
            jsonMain.addString("driver_id", driver_id);
            for (FieldModel fieldModel : model.getFieldList()) {

                if (ProfileViewActivity.imagePathModelList!=null && !ProfileViewActivity.imagePathModelList.isEmpty() && ProfileViewActivity.imagePathModelList.size()!=0) {
                    for (ImagePathModel imageModel : ProfileViewActivity.imagePathModelList) {
                        if (imageModel.getTitle().equals(model.getTitle())) {
                            if (fieldModel.getJson().has(P.image)) {
                                fieldModel.getJson().remove(P.image);
                                fieldModel.getJson().addString(P.image, imageModel.getPath());
                            } else {
                                fieldModel.getJson().addString(P.image, imageModel.getPath());
                            }
                        }
                    }
                }

                jsonChild.addJSON(model.getKey(), fieldModel.getJson());

//                        if (fieldModel.getJson().has("key")){
//                            fieldModel.getJson().remove("key");
//                        }else {
//                            fieldModel.getJson().addString("key","1");
//                        }

                fieldModel.getJson().addString("key", "1");

                if (!fieldModel.getJson().has(fieldModel.getFiled())) {
                    String valueKey = fieldModel.getFiled().replace("_", " ");
                    H.showMessage(context, getResources().getString(R.string.enter) + " " + capitalize(valueKey));
                    updateFlag = false;
                    return;
                }

                if (!fieldModel.getJson().has("image")) {
                    H.showMessage(context, getResources().getString(R.string.selectImage) + " " + model.getTitle());
                    updateFlag = false;
                    return;
                }
            }
            jsonMain.addJSON(P.document, jsonChild);

            if (jsonMain.getJson(P.document).toString().equals("{}")) {
                H.showMessage(context, getResources().getString(R.string.checkDocument));
            } else {
                if (updateFlag) {
//                    Log.e("TAG", "checkDocumentAASAS: "+ jsonMain.toString() );
                    dialog.dismiss();
                    hitSaveUserDocumentDetails(jsonMain);
                } else {
                    H.showMessage(context, getResources().getString(R.string.checkDocument));
                }
            }

        } catch (Exception e) {
            Log.e("TAG", "checkDocument: " + e.getMessage());
            H.showMessage(context, getResources().getString(R.string.somethingWrong));
        }
    }

    private void hitSaveUserDocumentDetails(Json j) {

        ProgressView.show(context, loadingDialog);
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
                        if (ProfileViewActivity.imagePathModelList != null) {
                            ProfileViewActivity.imagePathModelList.clear();
                        }
                        forAddData = false;
                        hitDocumentData(driver_id);
                        H.showMessage(context, getResources().getString(R.string.dataUpdated));
                    } else {
                        H.showMessage(context, json.getString(P.error_array));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitSaveUserDocumentDetails", session.getString(P.token));

    }

    private void checkData() {
        if (pendingDocumentModelList.isEmpty()) {
            binding.txtSaveDocument.setVisibility(View.GONE);
        } else {
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


    private String checkString(String string) {
        String value = "";
        if (string != null && !string.equals("null") && !string.equals("")) {
            value = string;
        }
        return value;
    }

    private void documentDialog(String imagePath) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_document_view);

        ImageView imgDocument = dialog.findViewById(R.id.imgDocument);
        LoadImage.glideString(context, imgDocument, imagePath);

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private void viewDialog(String imagePath) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_image_view);

        PhotoView imageView = dialog.findViewById(R.id.imageView);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);

        LoadImage.glideString(context, imageView, imagePath);

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

    public static AdditionalDriverDocumentFragment newInstance() {
        AdditionalDriverDocumentFragment fragment = new AdditionalDriverDocumentFragment();
        return fragment;
    }
}
