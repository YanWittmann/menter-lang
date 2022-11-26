package de.yanwittmann.matheval;

import de.yanwittmann.matheval.io.ExpressionFileReader;
import de.yanwittmann.matheval.lexer.Lexer;
import de.yanwittmann.matheval.operator.Operators;
import de.yanwittmann.matheval.parser.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class ParserTest {

    private final static Operators DEFAULT_OPERATORS = new Operators();

    @Test
    public void jonasExpressionParseTreeTest() {
        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ PARENTHESIS_PAIR\n" +
                               "   ├─ EXPRESSION: + (110 l r)\n" +
                               "   │  ├─ EXPRESSION: * (120 l r)\n" +
                               "   │  │  ├─ NUMBER_LITERAL: 4\n" +
                               "   │  │  └─ PARENTHESIS_PAIR\n" +
                               "   │  │     └─ EXPRESSION: + (110 l r)\n" +
                               "   │  │        ├─ NUMBER_LITERAL: 2\n" +
                               "   │  │        └─ NUMBER_LITERAL: 3\n" +
                               "   │  └─ FUNCTION_CALL\n" +
                               "   │     ├─ IDENTIFIER_ACCESSED\n" +
                               "   │     │  ├─ IDENTIFIER: foo\n" +
                               "   │     │  └─ IDENTIFIER: bar\n" +
                               "   │     └─ PARENTHESIS_PAIR\n" +
                               "   │        └─ EXPRESSION: + (110 l r)\n" +
                               "   │           ├─ NUMBER_LITERAL: 1\n" +
                               "   │           └─ NUMBER_LITERAL: 2\n" +
                               "   └─ FUNCTION_CALL\n" +
                               "      ├─ IDENTIFIER: hallo\n" +
                               "      └─ PARENTHESIS_PAIR\n" +
                               "         └─ EXPRESSION: + (110 l r)\n" +
                               "            ├─ NUMBER_LITERAL: 3\n" +
                               "            └─ NUMBER_LITERAL: 5\n" +
                               "STATEMENT\n" +
                               "└─ ASSIGNMENT: = (10 l r)\n" +
                               "   ├─ IDENTIFIER: foo\n" +
                               "   └─ FUNCTION_CALL\n" +
                               "      ├─ IDENTIFIER: hallo\n" +
                               "      └─ PARENTHESIS_PAIR\n" +
                               "         └─ EXPRESSION: + (110 l r)\n" +
                               "            ├─ NUMBER_LITERAL: 3\n" +
                               "            └─ NUMBER_LITERAL: 5",
                "(4 * (2 + 3) + foo.bar(1 + 2), hallo(3 + 5)); foo = hallo(3 + 5)");
    }

    @Test
    public void subsequentFunctionCallParseTreeTest() {
        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ FUNCTION_CALL\n" +
                               "   ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  │  ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  │  │  ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  │  │  │  ├─ IDENTIFIER: a\n" +
                               "   │  │  │  │  └─ IDENTIFIER: b\n" +
                               "   │  │  │  └─ IDENTIFIER_ACCESSED\n" +
                               "   │  │  │     ├─ IDENTIFIER: c\n" +
                               "   │  │  │     └─ IDENTIFIER: d\n" +
                               "   │  │  └─ IDENTIFIER: e\n" +
                               "   │  └─ IDENTIFIER_ACCESSED\n" +
                               "   │     ├─ IDENTIFIER: f\n" +
                               "   │     └─ IDENTIFIER_ACCESSED\n" +
                               "   │        ├─ STRING_LITERAL: \"g\"\n" +
                               "   │        └─ IDENTIFIER: h\n" +
                               "   └─ PARENTHESIS_PAIR\n" +
                               "      └─ IDENTIFIER: i",
                "a[b].c.d[e].f.\"g\".h(i)");
    }

    @Test
    public void lengthyStatementTest() {
        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ ASSIGNMENT: = (10 l r)\n" +
                               "   ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  │  ├─ IDENTIFIER: test\n" +
                               "   │  │  └─ EXPRESSION: + (110 l r)\n" +
                               "   │  │     ├─ STRING_LITERAL: \"varname-\"\n" +
                               "   │  │     └─ IDENTIFIER: test\n" +
                               "   │  └─ EXPRESSION: + (110 l r)\n" +
                               "   │     ├─ STRING_LITERAL: \"second\"\n" +
                               "   │     └─ FUNCTION_CALL\n" +
                               "   │        ├─ IDENTIFIER: foo\n" +
                               "   │        └─ PARENTHESIS_PAIR\n" +
                               "   └─ EXPRESSION: + (110 l r)\n" +
                               "      ├─ PARENTHESIS_PAIR\n" +
                               "      │  ├─ NUMBER_LITERAL: 3\n" +
                               "      │  └─ PARENTHESIS_PAIR\n" +
                               "      │     ├─ NUMBER_LITERAL: 5\n" +
                               "      │     └─ EXPRESSION: + (110 l r)\n" +
                               "      │        ├─ EXPRESSION: * (120 l r)\n" +
                               "      │        │  ├─ IDENTIFIER: PI\n" +
                               "      │        │  └─ NUMBER_LITERAL: 4\n" +
                               "      │        └─ FUNCTION_CALL\n" +
                               "      │           ├─ IDENTIFIER: sum\n" +
                               "      │           └─ PARENTHESIS_PAIR\n" +
                               "      │              └─ FUNCTION_CALL\n" +
                               "      │                 ├─ IDENTIFIER: map\n" +
                               "      │                 └─ PARENTHESIS_PAIR\n" +
                               "      │                    ├─ ARRAY\n" +
                               "      │                    │  ├─ NUMBER_LITERAL: 5\n" +
                               "      │                    │  ├─ NUMBER_LITERAL: 6\n" +
                               "      │                    │  └─ NUMBER_LITERAL: 7\n" +
                               "      │                    └─ IDENTIFIER: mapper\n" +
                               "      └─ ARRAY\n" +
                               "         ├─ NUMBER_LITERAL: 34\n" +
                               "         └─ FUNCTION_CALL\n" +
                               "            ├─ IDENTIFIER_ACCESSED\n" +
                               "            │  ├─ IDENTIFIER: foo\n" +
                               "            │  └─ IDENTIFIER: bar\n" +
                               "            └─ PARENTHESIS_PAIR\n" +
                               "               └─ IDENTIFIER: x",
                "test[\"varname-\" + test][\"second\" + foo()] = (3, (5, PI * 4 + sum(map([5, 6, 7], mapper)))) + [34, foo.bar(x)]");
    }

    @Test
    public void minorStatementsTest() {

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ EXPRESSION: + (110 l r)\n" +
                               "   ├─ EXPRESSION: + (110 l r)\n" +
                               "   │  ├─ NUMBER_LITERAL: 1\n" +
                               "   │  └─ EXPRESSION: * (120 l r)\n" +
                               "   │     ├─ NUMBER_LITERAL: 2\n" +
                               "   │     └─ NUMBER_LITERAL: 4\n" +
                               "   └─ NUMBER_LITERAL: 6",
                "1 + 2 * 4 + 6");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ ASSIGNMENT: = (10 l r)\n" +
                               "   ├─ IDENTIFIER: regex\n" +
                               "   └─ REGEX_LITERAL: r/regex/ig",
                "regex = r/regex/ig");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ ASSIGNMENT: = (10 l r)\n" +
                               "   ├─ IDENTIFIER: foo\n" +
                               "   └─ EXPRESSION: + (110 l r)\n" +
                               "      ├─ IDENTIFIER: var\n" +
                               "      └─ EXPRESSION: * (120 l r)\n" +
                               "         ├─ NUMBER_LITERAL: 2\n" +
                               "         └─ NUMBER_LITERAL: 4\n" +
                               "STATEMENT\n" +
                               "└─ EXPRESSION: + (110 l r)\n" +
                               "   ├─ NUMBER_LITERAL: 3\n" +
                               "   └─ NUMBER_LITERAL: 6",
                "foo = var + 2 * 4;3 + 6");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ PARENTHESIS_PAIR\n" +
                               "   ├─ NUMBER_LITERAL: 1\n" +
                               "   ├─ NUMBER_LITERAL: 2\n" +
                               "   ├─ NUMBER_LITERAL: 3\n" +
                               "   └─ PARENTHESIS_PAIR\n" +
                               "      ├─ NUMBER_LITERAL: 4\n" +
                               "      └─ NUMBER_LITERAL: 5",
                "(1,2,3,(4,5))");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ PARENTHESIS_PAIR\n" +
                               "   ├─ NUMBER_LITERAL: 1\n" +
                               "   ├─ NUMBER_LITERAL: 3\n" +
                               "   ├─ PARENTHESIS_PAIR\n" +
                               "   │  ├─ IDENTIFIER: foo\n" +
                               "   │  └─ NUMBER_LITERAL: 5\n" +
                               "   └─ EXPRESSION: + (110 l r)\n" +
                               "      ├─ NUMBER_LITERAL: 9\n" +
                               "      └─ NUMBER_LITERAL: 8",
                "(1, 3, (foo, 5), 9 + 8)");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ EXPRESSION: * (120 l r)\n" +
                               "   ├─ NUMBER_LITERAL: 1\n" +
                               "   └─ PARENTHESIS_PAIR\n" +
                               "      └─ EXPRESSION: + (110 l r)\n" +
                               "         ├─ NUMBER_LITERAL: 3\n" +
                               "         └─ NUMBER_LITERAL: 4",
                "1 * (3 + 4)");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ FUNCTION_CALL\n" +
                               "   ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  │  ├─ STRING_LITERAL: \"a\"\n" +
                               "   │  │  └─ IDENTIFIER: b\n" +
                               "   │  └─ IDENTIFIER: c\n" +
                               "   └─ PARENTHESIS_PAIR\n" +
                               "STATEMENT\n" +
                               "└─ FUNCTION_CALL\n" +
                               "   ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  │  ├─ IDENTIFIER: a\n" +
                               "   │  │  └─ IDENTIFIER: b\n" +
                               "   │  └─ IDENTIFIER: c\n" +
                               "   └─ PARENTHESIS_PAIR\n" +
                               "STATEMENT\n" +
                               "└─ FUNCTION_CALL\n" +
                               "   ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  │  ├─ IDENTIFIER: a\n" +
                               "   │  │  └─ IDENTIFIER: b\n" +
                               "   │  └─ IDENTIFIER: c\n" +
                               "   └─ PARENTHESIS_PAIR",
                "\"a\".b.c();a.b.c();a[b].c()");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ FUNCTION_CALL\n" +
                               "   ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  │  ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  │  │  ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  │  │  │  ├─ IDENTIFIER: a\n" +
                               "   │  │  │  │  └─ IDENTIFIER: b\n" +
                               "   │  │  │  └─ IDENTIFIER_ACCESSED\n" +
                               "   │  │  │     ├─ IDENTIFIER: c\n" +
                               "   │  │  │     └─ IDENTIFIER: d\n" +
                               "   │  │  └─ IDENTIFIER: e\n" +
                               "   │  └─ IDENTIFIER_ACCESSED\n" +
                               "   │     ├─ IDENTIFIER: f\n" +
                               "   │     └─ IDENTIFIER_ACCESSED\n" +
                               "   │        ├─ STRING_LITERAL: \"g\"\n" +
                               "   │        └─ IDENTIFIER: h\n" +
                               "   └─ PARENTHESIS_PAIR\n" +
                               "      └─ IDENTIFIER: i",
                "a[b].c.d[e].f.\"g\".h(i)");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ MAP\n" +
                               "   ├─ MAP_ELEMENT\n" +
                               "   │  ├─ IDENTIFIER: test\n" +
                               "   │  └─ IDENTIFIER: hello\n" +
                               "   └─ MAP_ELEMENT\n" +
                               "      ├─ IDENTIFIER: z\n" +
                               "      └─ STRING_LITERAL: \"test\"",
                "{test: hello, z: \"test\"}");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ ARRAY\n" +
                               "   ├─ NUMBER_LITERAL: 1\n" +
                               "   ├─ NUMBER_LITERAL: 2\n" +
                               "   └─ NUMBER_LITERAL: 3",
                "[1, 2, 3]");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ ARRAY\n" +
                               "   ├─ NUMBER_LITERAL: 1\n" +
                               "   ├─ ARRAY\n" +
                               "   │  ├─ NUMBER_LITERAL: 5\n" +
                               "   │  └─ NUMBER_LITERAL: 3\n" +
                               "   └─ NUMBER_LITERAL: 3",
                "[1, [5, 3], 3]");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ ARRAY\n" +
                               "   ├─ NUMBER_LITERAL: 34\n" +
                               "   └─ FUNCTION_CALL\n" +
                               "      ├─ IDENTIFIER_ACCESSED\n" +
                               "      │  ├─ IDENTIFIER: foo\n" +
                               "      │  └─ IDENTIFIER: bar\n" +
                               "      └─ PARENTHESIS_PAIR\n" +
                               "         └─ IDENTIFIER: x",
                "[34, foo.bar(x)]");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ FUNCTION_CALL\n" +
                               "   ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  ├─ IDENTIFIER: foo\n" +
                               "   │  └─ IDENTIFIER: bar\n" +
                               "   └─ PARENTHESIS_PAIR\n" +
                               "      ├─ NUMBER_LITERAL: 1\n" +
                               "      ├─ NUMBER_LITERAL: 2\n" +
                               "      └─ EXPRESSION: * (120 l r)\n" +
                               "         ├─ FUNCTION_CALL\n" +
                               "         │  ├─ IDENTIFIER: test\n" +
                               "         │  └─ PARENTHESIS_PAIR\n" +
                               "         │     └─ EXPRESSION: + (110 l r)\n" +
                               "         │        ├─ NUMBER_LITERAL: 3\n" +
                               "         │        └─ NUMBER_LITERAL: 6\n" +
                               "         └─ NUMBER_LITERAL: 4",
                "foo.bar(1, 2, test(3 + 6) * 4)");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ EXPRESSION: + (110 l r)\n" +
                               "   ├─ NUMBER_LITERAL: 4\n" +
                               "   └─ FUNCTION_CALL\n" +
                               "      ├─ IDENTIFIER: sum\n" +
                               "      └─ PARENTHESIS_PAIR\n" +
                               "         └─ ARRAY\n" +
                               "            ├─ NUMBER_LITERAL: 5\n" +
                               "            ├─ NUMBER_LITERAL: 6\n" +
                               "            └─ NUMBER_LITERAL: 7",
                "4 + sum([5, 6, 7])");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ PARENTHESIS_PAIR\n" +
                               "   ├─ NUMBER_LITERAL: 5\n" +
                               "   └─ EXPRESSION: + (110 l r)\n" +
                               "      ├─ NUMBER_LITERAL: 4\n" +
                               "      └─ FUNCTION_CALL\n" +
                               "         ├─ IDENTIFIER: map\n" +
                               "         └─ PARENTHESIS_PAIR\n" +
                               "            └─ ARRAY\n" +
                               "               ├─ NUMBER_LITERAL: 5\n" +
                               "               ├─ NUMBER_LITERAL: 6\n" +
                               "               └─ NUMBER_LITERAL: 7",
                "(5, 4 + map([5, 6, 7]))");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ ASSIGNMENT: = (10 l r)\n" +
                               "   ├─ LISTED_ELEMENTS\n" +
                               "   │  ├─ IDENTIFIER: un\n" +
                               "   │  └─ IDENTIFIER: pack\n" +
                               "   └─ FUNCTION_CALL\n" +
                               "      ├─ IDENTIFIER: foo\n" +
                               "      └─ PARENTHESIS_PAIR\n" +
                               "         └─ ARRAY\n" +
                               "            ├─ NUMBER_LITERAL: 1\n" +
                               "            └─ NUMBER_LITERAL: 2",
                "un, pack = foo([1, 2])");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ ASSIGNMENT: = (10 l r)\n" +
                               "   ├─ IDENTIFIER: var\n" +
                               "   └─ MAP\n" +
                               "      ├─ MAP_ELEMENT\n" +
                               "      │  ├─ IDENTIFIER: test\n" +
                               "      │  └─ NUMBER_LITERAL: 5\n" +
                               "      ├─ MAP_ELEMENT\n" +
                               "      │  ├─ IDENTIFIER: hmm\n" +
                               "      │  └─ EXPRESSION: * (120 l r)\n" +
                               "      │     ├─ STRING_LITERAL: \"wow\"\n" +
                               "      │     └─ NUMBER_LITERAL: 3\n" +
                               "      └─ MAP_ELEMENT\n" +
                               "         ├─ IDENTIFIER: rec\n" +
                               "         └─ MAP\n" +
                               "            └─ MAP_ELEMENT\n" +
                               "               ├─ IDENTIFIER: arr\n" +
                               "               └─ ARRAY\n" +
                               "                  ├─ NUMBER_LITERAL: 9\n" +
                               "                  └─ NUMBER_LITERAL: 2",
                "var = {test: 5, hmm: \"wow\" * 3, rec: {arr: [9, 2]}}");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ FUNCTION_CALL\n" +
                               "   ├─ IDENTIFIER_ACCESSED\n" +
                               "   │  ├─ IDENTIFIER: TestModule\n" +
                               "   │  └─ STRING_LITERAL: \"myFunction\"\n" +
                               "   └─ PARENTHESIS_PAIR\n" +
                               "      ├─ IDENTIFIER_ACCESSED\n" +
                               "      │  ├─ IDENTIFIER: TestModule\n" +
                               "      │  └─ STRING_LITERAL: \"myVariable\"\n" +
                               "      ├─ NUMBER_LITERAL: 3\n" +
                               "      └─ NUMBER_LITERAL: 6",
                "TestModule[\"myFunction\"](TestModule[\"myVariable\"], 3, 6)");

        assertParsedTreeEquals("IMPORT_STATEMENT\n" +
                               "└─ IDENTIFIER: TestModule",
                "import TestModule");

        assertParsedTreeEquals("IMPORT_STATEMENT\n" +
                               "└─ IDENTIFIER: TestModule",
                "import TestModule");

        assertParsedTreeEquals("IMPORT_AS_STATEMENT\n" +
                               "├─ IDENTIFIER: TestModule\n" +
                               "└─ IDENTIFIER: Test",
                "import TestModule as Test");

        assertParsedTreeEquals("IMPORT_INLINE_STATEMENT\n" +
                               "└─ IDENTIFIER: TestModule",
                "import TestModule inline");

        assertParsedTreeEquals("EXPORT_STATEMENT\n" +
                               "├─ ARRAY\n" +
                               "│  ├─ IDENTIFIER: myFunction\n" +
                               "│  └─ IDENTIFIER: myVariable\n" +
                               "└─ IDENTIFIER: TestModule",
                "export [myFunction, myVariable] as TestModule");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ EXPRESSION: - (110 l r)\n" +
                               "   ├─ FUNCTION_CALL\n" +
                               "   │  ├─ IDENTIFIER: test\n" +
                               "   │  └─ PARENTHESIS_PAIR\n" +
                               "   │     └─ NUMBER_LITERAL: 4\n" +
                               "   └─ NUMBER_LITERAL: 3",
                "test(4) - 3");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ FUNCTION_DECLARATION: = (10 l r)\n" +
                               "   ├─ IDENTIFIER: myFunction\n" +
                               "   ├─ PARENTHESIS_PAIR\n" +
                               "   │  ├─ IDENTIFIER: x\n" +
                               "   │  ├─ IDENTIFIER: y\n" +
                               "   │  └─ IDENTIFIER: z\n" +
                               "   └─ CODE_BLOCK\n" +
                               "      └─ EXPRESSION: - (110 l r)\n" +
                               "         ├─ EXPRESSION: + (110 l r)\n" +
                               "         │  ├─ IDENTIFIER: x\n" +
                               "         │  └─ FUNCTION_CALL\n" +
                               "         │     ├─ IDENTIFIER: pow\n" +
                               "         │     └─ PARENTHESIS_PAIR\n" +
                               "         │        ├─ IDENTIFIER: z\n" +
                               "         │        └─ NUMBER_LITERAL: 2\n" +
                               "         └─ IDENTIFIER: y",
                "myFunction(x, y, z) = x + pow(z, 2)\n - y");

        assertParsedTreeEquals("STATEMENT\n" +
                               "└─ FUNCTION_DECLARATION: = (10 l r)\n" +
                               "   ├─ IDENTIFIER: a\n" +
                               "   ├─ PARENTHESIS_PAIR\n" +
                               "   └─ CODE_BLOCK\n" +
                               "      ├─ FUNCTION_CALL\n" +
                               "      │  ├─ IDENTIFIER: b\n" +
                               "      │  └─ PARENTHESIS_PAIR\n" +
                               "      └─ FUNCTION_CALL\n" +
                               "         ├─ IDENTIFIER: c\n" +
                               "         └─ PARENTHESIS_PAIR",
                "a() = {b(); c()}");

        /*assertParsedTreeEquals("",
                "fib[n] = fib[n - 1] + fib[n - 2];");*/
    }

    @Test
    public void parseFileTest() throws IOException {
        ExpressionFileReader reader = new ExpressionFileReader(DEFAULT_OPERATORS);
        // reader.parse(new File("src/test/resources/modules/documentation/ImportModule1.me"));
        // reader.parse(new File("src/test/resources/modules/allFeatures/a.me"));
    }

    private void assertParsedTreeEquals(String expected, String expression) {
        Parser parser = new Parser(ParserTest.DEFAULT_OPERATORS);
        Lexer lexer = new Lexer(ParserTest.DEFAULT_OPERATORS);
        Assertions.assertEquals(
                expected,
                parser.toString(parser.parse(lexer.parse(expression))
                ));
    }
}