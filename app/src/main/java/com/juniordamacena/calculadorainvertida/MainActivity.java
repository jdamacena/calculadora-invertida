package com.juniordamacena.calculadorainvertida;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.juniordamacena.calculadorainvertida.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private EditText txtTela;
    private ActivityMainBinding binding;
    private boolean calculadoraInvertida = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        txtTela = binding.editText;

        // Caso a calculadora esteja no modo invertido, mostrar uma alerta
        if (calculadoraInvertida)
            mostrarAlertaCalculadoraInvertida();
    }

    /**
     * Método responsável por tratar o click dos botões para cálculo (listener adicionado via XML)
     */
    public void onClick(View view) {
        String operador = "";

        switch (view.getId()) {
            case R.id.btnResultado:
                // Obter a expressão numérica
                String expressaoBonita = txtTela.getText().toString();
                String expressaoComum = transformarExpressaoBonitaEmExpressaoComum(expressaoBonita);
                String expressaoInvertida = expressaoComum;
                String resultado;

                // Inverter a expressão
                if (calculadoraInvertida)
                    expressaoInvertida = transformarExpressaoComumEmExpressaoInvertida(expressaoComum);

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

                // Limpar a tela
                txtTela.setText("");
                break;
            case R.id.btnApagarUltCaractere:
                String expressao = txtTela.getText().toString();
                expressao = expressao.replaceAll("(.*).", "$1"); // Apagar o último caractere

                txtTela.setText(expressao);
                break;
            case R.id.btnAbreParentese:
                operador = "(";
                break;
            case R.id.btnFechaParentese:
                operador = ")";
                break;
            case R.id.btnMultiplicacao:
                operador = "×";
                break;
            case R.id.btnSubtracao:
                operador = "-";
                break;
            case R.id.btnDivisao:
                operador = "÷";
                break;
            case R.id.btnSoma:
                operador = "+";
                break;
            case R.id.btnNumNove:
                operador = "9";
                break;
            case R.id.btnNumOito:
                operador = "8";
                break;
            case R.id.btnNumSete:
                operador = "7";
                break;
            case R.id.btnNumSeis:
                operador = "6";
                break;
            case R.id.btnNumCinco:
                operador = "5";
                break;
            case R.id.btnNumQuatro:
                operador = "4";
                break;
            case R.id.btnNumTres:
                operador = "3";
                break;
            case R.id.btnNumDois:
                operador = "2";
                break;
            case R.id.btnNumUm:
                operador = "1";
                break;
            case R.id.btnNumZero:
                operador = "0";
                break;
            case R.id.btnPonto:
                operador = ".";
                break;
        }

        txtTela.append(operador);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calc_normal:
                String titulo;

                // Trocar entre calculadora normal e invertida
                if (!calculadoraInvertida) {

                    // Calculadora está invertida
                    mostrarAlertaCalculadoraInvertida();

                    titulo = "Calculadora Invertida";
                } else {
                    // Calculadora está normal
                    titulo = "Calculadora Comum";
                }

                configurarTituloActionbar(titulo);
                calculadoraInvertida = !calculadoraInvertida;
                break;
            case R.id.action_info:
                mostrarInformacaoApp();
                break;
        }

        return true;
    }

    /**
     * Colocar a sctring parâmetro como título da actionbar
     */
    private void configurarTituloActionbar(String titulo) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(titulo);
        }
    }

    /**
     * Mostrar informações sobre o desenvolvedor do app
     */
    private void mostrarInformacaoApp() {
        final String linkAppGitHub = getString(R.string.link_app_github);
        String mensagem = getString(R.string.aviso_info_app_mensagem);

        new AlertDialog.Builder(this)
                .setTitle(R.string.aviso_info_app_titulo)
                .setMessage(String.format(mensagem, linkAppGitHub))
                .setNeutralButton(R.string.aviso_info_app_txt_btn_aceitar, null)
                .setPositiveButton("GitHub", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(linkAppGitHub));
                        startActivity(intent);
                    }
                })
                .setIcon(R.drawable.ic_info2)
                .show();
    }

    /**
     * Mostrar um aviso informando que essa calculadora tem um comportamento não usual
     */
    private void mostrarAlertaCalculadoraInvertida() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.aviso_calc_invertida_titulo)
                .setMessage(R.string.aviso_calc_invertida_mensagem)
                .setNeutralButton(R.string.aviso_calc_invertida_txt_btn_aceitar, null)
                .setIcon(R.drawable.ic_aviso)
                .show();
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
