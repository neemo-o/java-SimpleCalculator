package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Controller {

	@FXML
	private TextField display;

	private StringBuilder expressao = new StringBuilder();
	private boolean resultadoMostrado = false;

	public void handleButton(javafx.event.ActionEvent event) {
		String valor = ((javafx.scene.control.Button) event.getSource()).getText();


		if (resultadoMostrado) {
			if (isOperador(valor) || valor.equals("x²") || valor.equals("xʸ")) {
				resultadoMostrado = false;
			} else {
				expressao.setLength(0);
				resultadoMostrado = false;
			}
		}

		if (isFuncaoMatematica(valor) && expressao.length() > 0
				&& Character.isDigit(expressao.charAt(expressao.length() - 1))) {
			expressao.append("*");
		}

		switch (valor) {
		case "sin":
			expressao.append("sin(");
			break;
		case "cos":
			expressao.append("cos(");
			break;
		case "tan":
			expressao.append("tan(");
			break;
		case "log":
			expressao.append("log(");
			break;
		case "ln":
			expressao.append("ln(");
			break;
		case "√":
			expressao.append("sqrt(");
			break;
		case "π":
			expressao.append("π");
			break;
		case "e":
			expressao.append("e");
			break;
		case "x²":
			expressao.append("^2");
			break;
		case "xʸ":
			expressao.append("^");
			break;
		case "%":
			expressao.append("%");
			break;
		case "x!":
			expressao.append("!");
			break;
		case "ˣ√":
			expressao.append("^(1/");
			break;
		default:
			expressao.append(valor);
			break;
		}

		display.setText(expressao.toString());
	}

	public void handleEquals() {
		String exp = expressao.toString();

		ExpressionEvaluator evaluator = new ExpressionEvaluator();
		double resultado = evaluator.avaliar(exp);
		display.setText(String.valueOf(resultado));

		expressao.setLength(0);
		expressao.append(resultado);
		resultadoMostrado = true;
	}

	public void handleClear() {
		expressao.setLength(0);
		display.setText("0");
		resultadoMostrado = false;
	}

	private boolean isOperador(String valor) {
		return valor.equals("+") || valor.equals("-") || valor.equals("×") || valor.equals("÷") || valor.equals("^");
	}

	private boolean isFuncaoMatematica(String valor) {
		return valor.equals("sin") || valor.equals("cos") || valor.equals("tan") || valor.equals("log")
				|| valor.equals("ln") || valor.equals("√");
	}
}
