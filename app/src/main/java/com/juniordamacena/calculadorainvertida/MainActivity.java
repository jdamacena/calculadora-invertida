package com.juniordamacena.calculadorainvertida;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.juniordamacena.calculadorainvertida.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtTela;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        txtTela = binding.editText;
    }

    /**
     * Método responsável por tratar o click dos botões para cálculo (listener adicionado via XML)
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnResultado:
                // Obter a expressão numérica
                String expressaoBonita = txtTela.getText().toString();
                String expressaoComum = transformarExpressaoBonitaEmExpressaoComum(expressaoBonita);
                String expressaoInvertida = transformarExpressaoComumEmExpressaoInvertida(expressaoComum);
                String resultado;

                try {
                    resultado = String.valueOf(new DoubleEvaluator().evaluate(expressaoInvertida));
                } catch (IllegalArgumentException e) {
                    resultado = "Erro matemático";
                } catch (Exception e) {
                    resultado = "Erro";
                }

                // Mostrar o resultado do cálculo
                binding.setResultado(resultado);

                break;
            case R.id.btnLimpar:
                txtTela.setText("");
                break;
            case R.id.btnMultiplicacao:
                txtTela.append("×");
                break;
            case R.id.btnSubtracao:
                txtTela.append("-");
                break;
            case R.id.btnDivisao:
                txtTela.append("÷");
                break;
            case R.id.btnSoma:
                txtTela.append("+");
                break;
            case R.id.btnNumNove:
                txtTela.append("9");
                break;
            case R.id.btnNumOito:
                txtTela.append("8");
                break;
            case R.id.btnNumSete:
                txtTela.append("7");
                break;
            case R.id.btnNumSeis:
                txtTela.append("6");
                break;
            case R.id.btnNumCinco:
                txtTela.append("5");
                break;
            case R.id.btnNumQuatro:
                txtTela.append("4");
                break;
            case R.id.btnNumTres:
                txtTela.append("3");
                break;
            case R.id.btnNumDois:
                txtTela.append("2");
                break;
            case R.id.btnNumUm:
                txtTela.append("1");
                break;
            case R.id.btnNumZero:
                txtTela.append("0");
                break;
        }
    }

    /**
     * Transforma uma expressão com operadores bonitos como 1x5÷14 em uma expressão que o método entende
     * como, por exemplo, 1*5/14
     *
     * @return expressão baseada na expressão parâmetro, mas já com os operadores que o computador entende
     * como /, *, etc
     */
    private String transformarExpressaoBonitaEmExpressaoComum(String expressaoBonita) {
        return expressaoBonita.replaceAll("÷", "/").replaceAll("×", "*");
    }

    /**
     * Esse método transforma uma expressão númerica comum (ex.: 1+1) em uma expressão
     * invertida, onde + significa - e * significa / - substitui-se os caracteres uns pelos
     * outros
     *
     * @param expressaoComum expressão como 1+4*2
     * @return expressão baseada na expressão parâmetro mas com os operadores trocados
     */
    private String transformarExpressaoComumEmExpressaoInvertida(String expressaoComum) {
        // Inverter "+" com "-" e "*" com "/"
        return expressaoComum
                .replace("+", "###")
                .replace("-", "+")
                .replace("###", "-")
                .replace("*", "###")
                .replace("/", "*")
                .replace("###", "/");
    }
}
