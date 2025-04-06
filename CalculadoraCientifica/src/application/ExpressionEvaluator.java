package application;

public class ExpressionEvaluator {

	public double avaliar(String expressao) {
		expressao = preprocessar(expressao);
		return avaliarExpressao(expressao);
	}

	private String preprocessar(String expr) {
		expr = expr.replace("π", String.valueOf(Math.PI));
        expr = expr.replace("e", String.valueOf(Math.E));
        expr = expr.replace("%", "/100");
		expr = expr.replace("×", "*").replace("÷", "/");
		expr = expr.replaceAll("([0-9.]+)!", "fact($1)");
		expr = expr.replaceAll("\\)", ")");
		return expr;
	}

	private double avaliarExpressao(String expr) {
		try {
			expr = tratarFuncoes(expr);
			final String expression = expr;

			return new Object() {
				int pos = -1, ch;

				void nextChar() {
					ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
				}

				boolean eat(int charToEat) {
					while (ch == ' ')
						nextChar();
					if (ch == charToEat) {
						nextChar();
						return true;
					}
					return false;
				}

				double parse() {
					nextChar();
					double x = parseExpression();
					if (pos < expression.length())
						throw new RuntimeException("Caractere inesperado: " + (char) ch);
					return x;
				}

				double parseExpression() {
					double x = parseTerm();
					while (true) {
						if (eat('+'))
							x += parseTerm();
						else if (eat('-'))
							x -= parseTerm();
						else
							return x;
					}
				}

				double parseTerm() {
					double x = parseFactor();
					while (true) {
						if (eat('*'))
							x *= parseFactor();
						else if (eat('/'))
							x /= parseFactor();
						else
							return x;
					}
				}

				double parseFactor() {
					if (eat('+'))
						return parseFactor();
					if (eat('-'))
						return -parseFactor();

					double x;
					int startPos = this.pos;

					if (eat('(')) {
						x = parseExpression();
						eat(')');
					} else if ((ch >= '0' && ch <= '9') || ch == '.') {
						while ((ch >= '0' && ch <= '9') || ch == '.')
							nextChar();
						x = Double.parseDouble(expression.substring(startPos, this.pos));
					} else if (ch >= 'a' && ch <= 'z') {
						while (ch >= 'a' && ch <= 'z')
							nextChar();
						String func = expression.substring(startPos, this.pos);
						x = parseFactor();
						x = aplicarFunc(func, x);
					} else {
						throw new RuntimeException("Fator inesperado: " + (char) ch);
					}

					if (eat('^'))
						x = Math.pow(x, parseFactor());

					return x;
				}
			}.parse();

		} catch (Exception e) {
			System.out.println("Erro ao avaliar expressão: " + e.getMessage());
			return 0;
		}
	}

	private String tratarFuncoes(String expr) {
		expr = expr.replaceAll("fact\\(([^)]+)\\)", "fact($1)");
		return expr;
	}

	private double aplicarFunc(String func, double val) {
		return switch (func) {
		case "sqrt" -> Math.sqrt(val);
		case "sin" -> Math.sin(Math.toRadians(val));
		case "cos" -> Math.cos(Math.toRadians(val));
		case "tan" -> Math.tan(Math.toRadians(val));
		case "log" -> Math.log10(val);
		case "ln" -> Math.log(val);
		case "fact" -> fatorial((int) val);
		default -> throw new RuntimeException("Função desconhecida: " + func);
		};
	}

	private double fatorial(int n) {
		if (n < 0)
			throw new ArithmeticException("Fatorial de negativo");
		double res = 1;
		for (int i = 2; i <= n; i++)
			res *= i;
		return res;
	}
}
