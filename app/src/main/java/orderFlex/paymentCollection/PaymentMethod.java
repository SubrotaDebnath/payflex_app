package orderFlex.paymentCollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class PaymentMethod extends AppCompatActivity {
    Button paySubmit;
    Spinner paymentMethod, bankList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method_form);

        paymentMethod=findViewById(R.id.paymentMethod);
        ArrayAdapter<CharSequence> methodSpinnerAdapter= ArrayAdapter.createFromResource(this,
                R.array.methode_list, android.R.layout.simple_spinner_item);
        methodSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethod.setAdapter(methodSpinnerAdapter);

        bankList=findViewById(R.id.bankList);
        ArrayAdapter<CharSequence> bankSpinnerAdapter= ArrayAdapter.createFromResource(this,
                R.array.bank_list, android.R.layout.simple_spinner_item);
        bankSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bankList.setAdapter(bankSpinnerAdapter);

        paySubmit=findViewById(R.id.paySubmit);
        paySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(PaymentMethod.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
