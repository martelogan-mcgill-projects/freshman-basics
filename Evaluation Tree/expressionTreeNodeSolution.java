import java.lang.Math.*;

class expressionTreeNodeSolution {
	private String value;
	private expressionTreeNodeSolution leftChild, rightChild, parent;

	expressionTreeNodeSolution() {
		value = null;
		leftChild = rightChild = parent = null;
	}

	// Constructor
	/*
	 * Arguments: String s: Value to be stored in the node
	 * expressionTreeNodeSolution l, r, p: the left child, right child, and
	 * parent of the node to created Returns: the newly created
	 * expressionTreeNodeSolution
	 */
	expressionTreeNodeSolution(String s, expressionTreeNodeSolution l,
			expressionTreeNodeSolution r, expressionTreeNodeSolution p) {
		value = s;
		leftChild = l;
		rightChild = r;
		parent = p;
	}

	/* Basic access methods */
	String getValue() {
		return value;
	}

	expressionTreeNodeSolution getLeftChild() {
		return leftChild;
	}

	expressionTreeNodeSolution getRightChild() {
		return rightChild;
	}

	expressionTreeNodeSolution getParent() {
		return parent;
	}

	/* Basic setting methods */
	void setValue(String o) {
		value = o;
	}

	// sets the left child of this node to n
	void setLeftChild(expressionTreeNodeSolution n) {
		leftChild = n;
		n.parent = this;
	}

	// sets the right child of this node to n
	void setRightChild(expressionTreeNodeSolution n) {
		rightChild = n;
		n.parent = this;
	}

	// Returns the root of the tree describing the expression s
	// Watch out: it makes no validity checks whatsoever!
	expressionTreeNodeSolution(String s) {
		// check if s contains parentheses. If it doesn't, then it's a leaf
		if (s.indexOf("(") == -1)
			setValue(s);
		else { // it's not a leaf

			/*
			 * break the string into three parts: the operator, the left
			 * operand, and the right operand. **
			 */
			setValue(s.substring(0, s.indexOf("(")));
			// delimit the left operand 2008
			int left = s.indexOf("(") + 1;
			int i = left;
			int parCount = 0;
			// find the comma separating the two operands
			while (parCount >= 0 && !(s.charAt(i) == ',' && parCount == 0)) {
				if (s.charAt(i) == '(')
					parCount++;
				if (s.charAt(i) == ')')
					parCount--;
				i++;
			}
			int mid = i;
			if (parCount < 0)
				mid--;

			// recursively build the left subtree
			setLeftChild(new expressionTreeNodeSolution(s.substring(left, mid)));

			if (parCount == 0) {
				// it is a binary operator
				// find the end of the second operand.F13
				while (!(s.charAt(i) == ')' && parCount == 0)) {
					if (s.charAt(i) == '(')
						parCount++;
					if (s.charAt(i) == ')')
						parCount--;
					i++;
				}
				int right = i;
				setRightChild(new expressionTreeNodeSolution(s.substring(
						mid + 1, right)));
			}
		}
	}

	// Returns a copy of the subtree rooted at this node... 2014
	expressionTreeNodeSolution deepCopy() {
		expressionTreeNodeSolution n = new expressionTreeNodeSolution();
		n.setValue(getValue());
		if (getLeftChild() != null)
			n.setLeftChild(getLeftChild().deepCopy());
		if (getRightChild() != null)
			n.setRightChild(getRightChild().deepCopy());
		return n;
	}

	// Returns a String describing the subtree rooted at a certain node.
	public String toString() {
		String ret = value;
		if (getLeftChild() == null)
			return ret;
		else
			ret = ret + "(" + getLeftChild().toString();
		if (getRightChild() == null)
			return ret + ")";
		else
			ret = ret + "," + getRightChild().toString();
		ret = ret + ")";
		return ret;
	}

	// Evaluates binary operation
	public static double operate(String op, double x, double y) {
		if (op.equals("add"))
			return x + y;
		if (op.equals("minus"))
			return x - y;
		if (op.equals("mult"))
			return x * y;
		if (op.equals("sin"))
			return Math.sin(x);
		if (op.equals("cos"))
			return Math.cos(x);
		if (op.equals("exp"))
			return Math.exp(x);
		else
			throw new IllegalArgumentException();
	}

	// Returns the value of the expression rooted at a given node
	// when x has a certain value
	double evaluate(double x) {

		// Check for leaf
		if (this.getLeftChild() == null) {
			if ((this.getValue()).equals("x"))
				return x;
			else
				return Double.parseDouble(this.getValue());
		} else {
			if (this.getRightChild() == null) { // evaluate left subtree
				return operate(this.getValue(),
						this.getLeftChild().evaluate(x), 0);
			} else { // evaluate subtree rooted at this node
				return operate(this.getValue(),
						this.getLeftChild().evaluate(x), this.getRightChild()
								.evaluate(x));
			}
		}
	}

	/*
	 * returns the root of a new expression tree representing the derivative of
	 * the original expression
	 */
	expressionTreeNodeSolution differentiate() {

		// Check for leaf
		if (getLeftChild() == null) {
			if (this.getValue().equals("x"))
				return new expressionTreeNodeSolution("1");
			else
				return new expressionTreeNodeSolution("0");
		}

		// Set variables for current node and parent node
		String current = this.getValue();
		expressionTreeNodeSolution parent = this.getParent();

		// Declare variables for left and right subtrees
		expressionTreeNodeSolution left;
		expressionTreeNodeSolution right;

		// Find derivative tree for polynomial expression
		if (current.equals("add") || current.equals("minus")) {
			left = this.getLeftChild().differentiate();
			right = this.getRightChild().differentiate();

			return new expressionTreeNodeSolution(current, left, right, parent);
		}

		if (current.equals("mult")) {
			// find tree for d/dx (f(x)) * g(x)
			left = new expressionTreeNodeSolution("mult", this.getLeftChild()
					.differentiate(), this.getRightChild().deepCopy(), parent);
			// find tree for d/dx (g(x)) * f(x)
			right = new expressionTreeNodeSolution("mult", this.getLeftChild()
					.deepCopy(), this.getRightChild().differentiate(), parent);
			// add left and right subtree expressions
			return new expressionTreeNodeSolution("add", left, right, parent);
		}

		if (current.equals("sin")) {
			// take cosine of original expression
			left = new expressionTreeNodeSolution("cos", this.getLeftChild()
					.deepCopy(), null, parent);
			// take derivative of original expression
			right = this.getLeftChild().differentiate();
			// multiply left and right subtree expressions
			return new expressionTreeNodeSolution("mult", left, right, parent);
		}

		if (current.equals("cos")) {
			// take negative sine of original expression
			expressionTreeNodeSolution sine = new expressionTreeNodeSolution(
					"sin", this.getLeftChild().deepCopy(), null, parent);
			left = new expressionTreeNodeSolution("minus", sine, null, parent);
			// take derivative of original expression
			right = this.getLeftChild().differentiate();
			// multiply left and right subtree expressions
			return new expressionTreeNodeSolution("mult", left, right, parent);
		}
		
		if (current.equals("exp")) {
			// copy tree for expression e^(f(x))
			left = this.deepCopy();
			// find tree for d/dx f(x)
			right = this.getLeftChild().differentiate();
			// multiply left and right subtree expressions 
			return new expressionTreeNodeSolution("mult", left, right, parent);
		}
		
		// else, an error occurred
		return null;
	}

	public static void main(String args[]) {
		expressionTreeNodeSolution e = new expressionTreeNodeSolution(
				"mult(add(2,x),cos(x))");
		System.out.println(e);
		System.out.println(e.evaluate(1));
		System.out.println(e.differentiate());
	}
}