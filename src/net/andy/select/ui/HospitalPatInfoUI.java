package net.andy.select.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.andy.boiling.R;
import net.andy.boiling.domain.PrescriptionDomain;

/**
 * Created by Guang on 2016/6/8.
 */
public class HospitalPatInfoUI extends Activity{
    private LinearLayout adjustPatientInfo_linearLayout;
    private TextView adjustPatientinfo_drugstoreName_textView;
    private TextView adjustPatientInfo_classification_textView;
    private TextView adjustPatientInfo_info_textView;
    private TextView adjustPatient_date_textView;
    private TextView adjustPatient_patientNo_textView;
    private TextView adjustPatientInfo_patientName_textView;
    private TextView adjustPatient_deptName_textView;
    private TextView adjustPatient_doctorName_textView;
    private TextView adjustPatientInfo_diagnosis_textView;
    private PrescriptionDomain prescriptionDomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dis_patientinfo);
        adjustPatientInfo_linearLayout= (LinearLayout) findViewById(R.id.adjustPatientInfo_linearLayout);
        adjustPatientinfo_drugstoreName_textView= (TextView) findViewById(R.id.adjustPatientinfo_drugstoreName_textView);
        adjustPatientInfo_classification_textView= (TextView) findViewById(R.id.adjustPatientInfo_classification_textView);
        adjustPatientInfo_info_textView= (TextView) findViewById(R.id.adjustPatientInfo_info_textView);
        adjustPatient_date_textView= (TextView) findViewById(R.id.adjustPatient_date_textView);
        adjustPatient_patientNo_textView= (TextView) findViewById(R.id.adjustPatient_patientNo_textView);
        adjustPatientInfo_patientName_textView= (TextView) findViewById(R.id.adjustPatientInfo_patientName_textView);
        adjustPatient_deptName_textView= (TextView) findViewById(R.id.adjustPatient_deptName_textView);
        adjustPatient_doctorName_textView= (TextView) findViewById(R.id.adjustPatient_doctorName_textView);
        adjustPatientInfo_diagnosis_textView= (TextView) findViewById(R.id.adjustPatientInfo_diagnosis_textView);
        adjustPatientInfo_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent in=getIntent();
        prescriptionDomain= (PrescriptionDomain) in.getSerializableExtra("pre");
        setValue();
    }
    private void setValue(){
        System.out.println(prescriptionDomain.getDoctorName());
        adjustPatientinfo_drugstoreName_textView.setText(prescriptionDomain.getDrugstoreName());
        adjustPatientInfo_classification_textView.setText(prescriptionDomain.getClassification());
        adjustPatientInfo_info_textView.setText(prescriptionDomain.getPresNumber()+"付 "+prescriptionDomain.getWay()+"  "+prescriptionDomain.getManufacture()+"  "+prescriptionDomain.getFrequency());
        adjustPatient_date_textView.setText(prescriptionDomain.getDate());
        adjustPatient_patientNo_textView.setText(prescriptionDomain.getPatientNo());
        adjustPatientInfo_patientName_textView.setText(prescriptionDomain.getPatientName());
        adjustPatient_deptName_textView.setText(prescriptionDomain.getDeptName());
        adjustPatient_doctorName_textView.setText(prescriptionDomain.getDoctorName());
        adjustPatientInfo_diagnosis_textView.setText(prescriptionDomain.getDiagnosis());
    }
}
