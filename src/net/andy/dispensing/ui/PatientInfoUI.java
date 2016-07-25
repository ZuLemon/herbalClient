package net.andy.dispensing.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.andy.boiling.R;
import net.andy.boiling.domain.PrescriptionDomain;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 患者信息
 * Created by Guang on 2016/3/16.
 */
public class PatientInfoUI extends Activity{
    @ViewInject(R.id.adjustPatientInfo_linearLayout)
    private LinearLayout adjustPatientInfo_linearLayout;
    @ViewInject(R.id.adjustPatientinfo_drugstoreName_textView)
    private TextView adjustPatientinfo_drugstoreName_textView;
    @ViewInject(R.id.adjustPatientInfo_classification_textView)
    private TextView adjustPatientInfo_classification_textView;
    @ViewInject(R.id.adjustPatientInfo_info_textView)
    private TextView adjustPatientInfo_info_textView;
    @ViewInject(R.id.adjustPatient_date_textView)
    private TextView adjustPatient_date_textView;
    @ViewInject(R.id.adjustPatient_patientNo_textView)
    private TextView adjustPatient_patientNo_textView;
    @ViewInject(R.id.adjustPatientInfo_patientName_textView)
    private TextView adjustPatientInfo_patientName_textView;
    @ViewInject(R.id.adjustPatient_deptName_textView)
    private TextView adjustPatient_deptName_textView;
    @ViewInject(R.id.adjustPatient_doctorName_textView)
    private TextView adjustPatient_doctorName_textView;
    @ViewInject(R.id.adjustPatientInfo_diagnosis_textView)
    private TextView adjustPatientInfo_diagnosis_textView;
    private PrescriptionDomain prescriptionDomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospitalpatinfo);
        x.view().inject(this);
        Intent in=getIntent();
        prescriptionDomain= (PrescriptionDomain) in.getSerializableExtra("pre");
        setValue();
    }
    @Event(R.id.adjustPatientInfo_linearLayout)
    private void btnClick(View view) {
        finish();
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
