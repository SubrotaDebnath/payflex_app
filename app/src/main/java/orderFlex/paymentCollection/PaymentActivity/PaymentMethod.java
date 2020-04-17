package orderFlex.paymentCollection.PaymentActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import orderFlex.paymentCollection.Model.APICallings.PullPaymentMethods;
import orderFlex.paymentCollection.Model.APICallings.PushBills;
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentRequestBody;
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentMothodsResponse;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.SharedPrefManager;

public class PaymentMethod extends AppCompatActivity implements PullPaymentMethods.PaymentMethodsListener,
        AdapterView.OnItemSelectedListener, PushBills.PushBillListener {
    private Button paySubmit;
    private Spinner spinnerMethod, spinnerBank;
    private SharedPrefManager prefManager;
    private Helper helper;
    private PullPaymentMethods pullPaymentMethods;
    private List<PaymentMothodsResponse.BankList> bankListData =new ArrayList<>();
    private List<PaymentMothodsResponse.PaymentMethode> methodListData =new ArrayList<>();
    private String TAG="PaymentMethod";
    private View containerView;
    private BillPaymentRequestBody requestBody;
    private EditText referenceNo,payAmount;
    private TextView payDate;
    private String orderCode;
    private PushBills pushBills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.payment_method_form);
        setContentView(R.layout.payment_form_test);

        prefManager=new SharedPrefManager(this);
        helper=new Helper(this);
        containerView=findViewById(R.id.paymentMethodeActivity);
        referenceNo=findViewById(R.id.referenceNo);
        payDate=findViewById(R.id.payDate);
        payAmount=findViewById(R.id.payAmount);
        payDate.setText(helper.getDate());
        requestBody=new BillPaymentRequestBody();

        pushBills=new PushBills(this);

        Intent intent=getIntent();
        orderCode=intent.getStringExtra("order_id");
        //Log.i(TAG,orderCode);

        pullPaymentMethods=new PullPaymentMethods(this);
        if (helper.isInternetAvailable()){
            pullPaymentMethods.paymentMethodsCall(prefManager.getUsername(),prefManager.getUserPassword());
        }else {
            helper.showSnakBar(containerView,"Please check your internet connection!");
        }

        spinnerMethod =findViewById(R.id.paymentMethod);
        spinnerMethod.setOnItemSelectedListener(this);
        spinnerBank =findViewById(R.id.bankList);
        spinnerBank.setOnItemSelectedListener(this);

        paySubmit=findViewById(R.id.paySubmit);
        paySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String refNo=referenceNo.getText().toString();
                String payed=payAmount.getText().toString();
                requestBody.setImageId("");
                requestBody.setOrderCode(orderCode);
                requestBody.setPaymentDateTime(payDate.getText().toString());
                requestBody.setReferenceNo(refNo);
                requestBody.setTrxid(helper.makeUniqueID());
                requestBody.setAmount(payed);
                if (refNo.isEmpty()||payed.isEmpty()){
                    helper.showSnakBar(containerView,"Some fields are empty!");
                }else {
                    if (helper.isInternetAvailable()){
                        pushBills.pushBillCall(prefManager.getUsername(),prefManager.getUserPassword(),requestBody);
                    }else {
                        helper.showSnakBar(containerView,"Please check your internet connection!");
                    }
                }
                //requestBody.set
            }
        });
    }

    @Override
    public void onPreResponse(PaymentMothodsResponse response, int code) {
        bankListData =response.getBankList();
        methodListData =response.getPaymentMethode();
        List<String> bankList=new ArrayList<>();
        List<String>methodList=new ArrayList<>();

        if (response!=null && code==202){
            for (PaymentMothodsResponse.BankList bank:response.getBankList()) {
                bankList.add(bank.getBankName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,bankList);
            spinnerBank.setAdapter(adapter);
            spinnerBank.setSelection(bankList.indexOf(0));

            for (PaymentMothodsResponse.PaymentMethode methode:response.getPaymentMethode()) {
                methodList.add(methode.getMethodeName());
            }
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,methodList);
            spinnerMethod.setAdapter(adapter2);
            spinnerMethod.setSelection(bankList.indexOf(0));
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId()==R.id.bankList){
            Log.i(TAG,"Bank");
            requestBody.setFinancial_institution_id(bankListData.get(position).getId());
        }
        if (parent.getId()==R.id.paymentMethod){
            Log.i(TAG,"Method");
            requestBody.setPaymentModeId(methodListData.get(position).getId());
        }
        Log.i(TAG,bankListData.get(position).getBankName());
        Log.i(TAG,"Selected: "+position);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//
    }
    @Override
    public void onResponse(BillPaymentResponse response, int code) {
        if (code==202){
            helper.showSnakBar(containerView,"Successfully submitted!");
        }else {
            helper.showSnakBar(containerView,"Failed to submit!");
        }
    }
}
