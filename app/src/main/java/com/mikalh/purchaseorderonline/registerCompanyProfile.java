package com.mikalh.purchaseorderonline;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;


import com.github.kimkevin.cachepot.CachePot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.mikalh.purchaseorderonline.Model.Company;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link registerCompanyProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link registerCompanyProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class registerCompanyProfile extends android.support.v4.app.Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String COMPANY_KEY = "company";
    registerUser next = new registerUser();
    private String mParam1;
    private String mParam2;
    String[] listProvince = new String[]{
            "Aceh" ,
            "Bali" ,
            "Banten" ,
            "Bengkulu" ,
            "Gorontalo" ,
            "DKI Jakarta" ,
            "Jambi" ,
            "Jawa Barat" ,
            "Jawa Tengah" ,
            "Jawa Timur" ,
            "Kalimantan Barat" ,
            "Kalimantan Selatan" ,
            "Kalimantan Tengah" ,
            "Kalimantan Timur" ,
            "Kalimantan Utara" ,
            "Kepulauan Bangka Belitung" ,
            "Kepulauan Riau" ,
            "Lampung" ,
            "Maluku" ,
            "Maluku Utara" ,
            "Nusa Tenggara Barat" ,
            "Nusa Tenggara Timur" ,
            "Papua" ,
            "Papua Barat" ,
            "Riau" ,
            "Sulawesi Barat" ,
            "Sulawesi Selatan" ,
            "Sulawesi Tengah" ,
            "Sulawesi Tenggara" ,
            "Sulawesi Utara" ,
            "Sumatera Barat" ,
            "Sumatera Selatan" ,
            "Sumatera Utara" ,
            "Yogyakarta"

    };
    private OnFragmentInteractionListener mListener;
    Button nextDo;
    ViewPager myViewPagger;
    TextInputEditText companyName_register, adress_register
            ,city_register,telephone_register,fax_register;
    SearchableSpinner province_register;
    public registerCompanyProfile() {
        // Required empty public constructor
    }

    public static registerCompanyProfile newInstance(String param1, String param2) {
        registerCompanyProfile fragment = new registerCompanyProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    SharedPreferences mPrefs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
    }
    TextInputLayout TIL_company_register
            , TIL_adress_register,TIL_city_register,TIL_telephone_register,TIL_fax_register;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_company_profile, container, false);
        city_register = view.findViewById(R.id.city_register);
        companyName_register = view.findViewById(R.id.companyName_register);
        adress_register = view.findViewById(R.id.adress_register);
        telephone_register = view.findViewById(R.id.telephone_register);
        fax_register = view.findViewById(R.id.fax_register);
        nextDo = view.findViewById(R.id.nextDo);
        nextDo.setOnClickListener(this);
        TIL_adress_register = view.findViewById(R.id.TIL_adress_register);
        TIL_city_register = view.findViewById(R.id.TIL_city_register);
        TIL_company_register = view.findViewById(R.id.TIL_company_register);
        TIL_fax_register = view.findViewById(R.id.TIL_fax_register);
        TIL_telephone_register = view.findViewById(R.id.TIL_telephone_register);
        province_register = view.findViewById(R.id.province_register);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,listProvince);
        province_register.setAdapter(adapter);
        province_register.setTitle("Select Province");
        myViewPagger = getActivity().findViewById(R.id.myViewPagger);

        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

  /*  @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view == nextDo){
            Boolean next = true;
            TIL_company_register.setError(null);
            TIL_company_register.setErrorEnabled(false);
            TIL_telephone_register.setError(null);
            TIL_telephone_register.setErrorEnabled(false);
            TIL_fax_register.setError(null);
            TIL_fax_register.setErrorEnabled(false);
            TIL_city_register.setError(null);
            TIL_city_register.setErrorEnabled(false);
            TIL_adress_register.setError(null);
            TIL_adress_register.setErrorEnabled(false);
            if (companyName_register.getText().toString().isEmpty()){
                TIL_fax_register.setErrorEnabled(true);
                TIL_fax_register.setError("Semua Data Harus Diisi");
                next = false;
            }if (adress_register.getText().toString().isEmpty()){
                TIL_fax_register.setErrorEnabled(true);
                TIL_fax_register.setError("Semua Data Harus Diisi");
                next = false;
            }if (city_register.getText().toString().isEmpty()){
                TIL_fax_register.setErrorEnabled(true);
                TIL_fax_register.setError("Semua Data Harus Diisi");
                next = false;
            }if (fax_register.getText().toString().isEmpty()){
                TIL_fax_register.setErrorEnabled(true);
                TIL_fax_register.setError("Semua Data Harus Diisi");
                next = false;
            }if (telephone_register.getText().toString().isEmpty()){
                TIL_fax_register.setErrorEnabled(true);
                TIL_fax_register.setError("Semua Data Harus Diisi");
                next = false;
            }
            if (next) {
                Gson gson = new Gson();
                Company company = new Company();
                company.setNama_perusahaan(companyName_register.getText().toString());
                company.setAlamat_perusahaan(adress_register.getText().toString());
                company.setKota(city_register.getText().toString());
                company.setNo_fax(fax_register.getText().toString());
                company.setProvinsi(province_register.getSelectedItem().toString());
                company.setNomorTelphone(telephone_register.getText().toString());

                SharedPreferences.Editor prefEditor = mPrefs.edit();
                String jsonNext = gson.toJson(company);
                prefEditor.putString("Company", jsonNext);
                prefEditor.commit();



            /*Bundle bundle = new Bundle();
            bundle.putSerializable(COMPANY_KEY,company);
            next.setArguments(bundle);
            CachePot.getInstance().push(company);*/


                myViewPagger.setCurrentItem(1, true);
            }
        }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
