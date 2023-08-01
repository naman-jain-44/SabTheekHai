package com.example.healthtrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import androidx.appcompat.app.AppCompatActivity;
import org.tensorflow.lite.Interpreter;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {
    Interpreter tflite;
    EditText inp1;

    EditText inp2;
    EditText inp3;
    EditText inp4;
    EditText inp5;
    EditText inp6;

    TextView outp;
    Button pred;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pred=findViewById(R.id.searchButton);
        outp=findViewById(R.id.resultTextView);
        inp1=findViewById(R.id.editText1);
        inp2=findViewById(R.id.editText2);
        inp3=findViewById(R.id.editText3);
        inp4=findViewById(R.id.editText4);
        inp5=findViewById(R.id.editText5);
        inp6=findViewById(R.id.editText6);

        try {
            tflite = new Interpreter(loadModelFile());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        pred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float prediction=doInference(inp1.getText().toString(),inp2.getText().toString(),inp3.getText().toString(),inp4.getText().toString(),inp5.getText().toString(),inp6.getText().toString());
               // System.out.println(prediction);
                float compress=sigmoid(prediction);
                if(compress<=0.5){
                    outp.setText("Successfull Treatment ");
                }else{
                    outp.setText("Not Treated Successfull");
                }


            }
        });

    }
    public static float sigmoid(float x) {
        return (float) (1 / (1 + Math.exp(-x)));
    }
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor=this.getAssets().openFd("regression.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startOffset=fileDescriptor.getStartOffset();
        long declareLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declareLength);
    }
    private float doInference(String inp1,String inp2,String inp3,String inp4,String inp5,String inp6) {
        float[] inputVal=new float[6];
        inputVal[0]=Float.parseFloat(inp1);
        inputVal[1]=Float.parseFloat(inp2);
        inputVal[2]=Float.parseFloat(inp3);
        inputVal[3]=Float.parseFloat(inp4);
        inputVal[4]=Float.parseFloat(inp5);
        inputVal[5]=Float.parseFloat(inp6);

        float[][] output=new float[1][1];
        tflite.run(inputVal,output);
        return output[0][0];
    }


}