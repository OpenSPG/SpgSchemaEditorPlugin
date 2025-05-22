package org.openspg.idea.lang.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;import com.intellij.ui.mac.foundation.ID;

/* Auto generated File */
%%

%class SchemaLexer
%implements com.intellij.lexer.FlexLexer, org.openspg.idea.schema.grammar.psi.SchemaTypes
%unicode
%public
%column

%function advance
%type IElementType

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////// USER CODE //////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

%{
    /**
     * The number of open but not closed braces.
     * Note: lexer does not distinguish braces from brackets while counting them.
     */
    private int myBraceCount = 0;

    private final int[] indentPos = {0, 0, 0, 0, 0, 0};
    private final int[] indentState = {DEFINITION_STATE, KV_STATE, DEFINITION_STATE, KV_STATE, DEFINITION_STATE, KV_STATE};
    private final IElementType[] indentToken = {INDENT, INDENT_META, INDENT_PROP, INDENT_PROPMETA, INDENT_SUBPROP, INDENT_SUBPROPMETA};
    private final int maxIndentLevel = 6;
    private int currentIndentLevel = 0;

    //-------------------------------------------------------------------------------------------------------------------

    /** @param offset offset from currently matched token start (could be negative) */
    private char getCharAtOffset(final int offset) {
        final int loc = getTokenStart() + offset;
        return 0 <= loc && loc < zzBuffer.length() ? zzBuffer.charAt(loc) : (char) -1;
    }

    private boolean isAfterEol() {
        final char prev = getCharAtOffset(-1);
        return prev == (char)-1 || prev == '\n';
    }

    private int getIndentLevel() {
        assert isAfterEol();
        int currentIndentPos = yylength();
        for (int i = 0; i < yylength(); i+=1) {
            if (getCharAtOffset(i) == '\t') {
                currentIndentPos += 1;
            }
        }

        int lastIndentPos = this.indentPos[this.currentIndentLevel];
        if (currentIndentPos > lastIndentPos) {
            if (this.currentIndentLevel == maxIndentLevel) {
                return -1;
            }
            this.currentIndentLevel ++;
            this.indentPos[this.currentIndentLevel] = currentIndentPos;
            return this.currentIndentLevel;

        } else if (currentIndentPos < lastIndentPos) {
            for (int i = 0; i < this.currentIndentLevel; i++) {
                if (this.indentPos[i] == currentIndentPos) {
                    this.currentIndentLevel = i;
                    return this.currentIndentLevel;
                }
            }
            return -1;
        }
        return this.currentIndentLevel;
    }

    private void goToState(int state) {
        yybegin(state);
        yypushback(yylength());
    }

    //-------------------------------------------------------------------------------------------------------------------
    private void trace(String tag) {
        int tokenStart = getTokenStart();
        int tokenEnd = Math.min(tokenStart + 40, zzBuffer.length());
        System.out.println("====" + tag + "\n{{ " + zzBuffer.subSequence(tokenStart, tokenEnd) + " }}");
    }
%}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////// REGEXPS DECLARATIONS //////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// NB !(!a|b) is "a - b"
// From the spec
ANY_CHAR = [^]

// Schema spec: when a comment follows another syntax element,
//  it must be separated from it by space characters.
// See http://www.yaml.org/spec/1.2/spec.html#comment
COMMENT =                       "#"{LINE}

IDENTIFIER = [:jletter:] [:jletterdigit:]*
LETTERDIGIT = [:jletterdigit:]+

DOUBLE_QUOTED_STRING = \"([^\\\"\r\n]|\\[^\r\n])*\"?
SINGLE_QUOTED_STRING = '([^\\'\r\n]|\\[^\r\n])*'?
GRAVE_QUOTED_STRING = \`([^\\`\r\n]|\\[^\r\n])*\`?

WHITE_SPACE_CHAR = [ \t]
WHITE_SPACE = {WHITE_SPACE_CHAR}+
EOL = "\n"

BLANK_LINE = {WHITE_SPACE_CHAR}*{EOL}
LINE = [^\n]*
COMMENT = "#"{LINE}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////// STATES DECLARATIONS //////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// Main states
%xstate LINE_START_STATE, PLAIN_BLOCK_STATE

// Small technical one-token states
%xstate NAMESPACE_STATE, LINE_COMMENT_STATE, ERROR_STATE

%xstate ENTITY_STATE, WAITING_ENTITY_ALIAS_NAME_STATE, WAITING_ENTITY_CLASS_STATE
%xstate ENTITYMETA_STATE

%xstate PROPERTY_STATE, WAITING_PROPERTY_ALIAS_NAME_STATE, WAITING_PROPERTY_CLASS_STATE
%xstate PROPERTYMETA_STATE

%xstate WAITING_META_VALUE_STATE, WAITING_META_BUILTIN_VALUE_STATE, WAITING_META_TEXT_VALUE_STATE

%xstate DEFINITION_STATE, KV_STATE, WAITING_VALUE_STATE, WAITING_BLOCK_VALUE_STATE
%%
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////// RULES declarations ////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//-------------------------------------------------------------------------------------------------------------------
// State in the start of new line in block mode
<YYINITIAL, LINE_START_STATE> {
    // It is a text, go next state and process it there
    "namespace" {
          this.currentIndentLevel = 0;
          goToState(NAMESPACE_STATE);
      }

    {BLANK_LINE} {
          return TokenType.WHITE_SPACE;
      }

    {WHITE_SPACE}* {COMMENT} {
          return LINE_COMMENT;
      }

    {EOL} {
          return TokenType.WHITE_SPACE;
      }

    {WHITE_SPACE} {
          int indentLevel = this.getIndentLevel();
          if (indentLevel < 0) {
              goToState(ERROR_STATE);
          } else {
              yybegin(this.indentState[indentLevel]);
              yypushback(yylength() - 1);
              return this.indentToken[indentLevel];
          }
      }

    {ANY_CHAR} {
          this.currentIndentLevel = 0;
          goToState(DEFINITION_STATE);
      }
}
//-------------------------------------------------------------------------------------------------------------------


//-------------------------------------------------------------------------------------------------------------------
// common: white-space, eol, comment
<NAMESPACE_STATE, ERROR_STATE, DEFINITION_STATE, KV_STATE, WAITING_VALUE_STATE> {
    {EOL} {
          yybegin(LINE_START_STATE);
          return TokenType.WHITE_SPACE;
      }

    {COMMENT} {
          return COMMENT;
      }

    {WHITE_SPACE} {
          return TokenType.WHITE_SPACE;
      }
}

<LINE_COMMENT_STATE> {
    {EOL} {
          yybegin(LINE_START_STATE);
          return TokenType.WHITE_SPACE;
      }

    {WHITE_SPACE} {
          return TokenType.WHITE_SPACE;
      }

    {COMMENT} {
          return LINE_COMMENT;
      }
}
//-------------------------------------------------------------------------------------------------------------------


//-------------------------------------------------------------------------------------------------------------------
// namespace
<NAMESPACE_STATE> {
    "namespace" {
        return NAMESPACE_KEYWORD;
    }

    {IDENTIFIER} {
          return IDENTIFIER;
      }
}
//-------------------------------------------------------------------------------------------------------------------


//-------------------------------------------------------------------------------------------------------------------
// common error line
<ERROR_STATE> {
    {ANY_CHAR} {
          return TokenType.BAD_CHARACTER;
      }
}
//-------------------------------------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------------------------------------
// definition-state
// name (alias-name) : type
<DEFINITION_STATE> {
    {DOUBLE_QUOTED_STRING}   { return STRING_LITERAL;  }
    {SINGLE_QUOTED_STRING}   { return STRING_LITERAL;  }
    {GRAVE_QUOTED_STRING}   { return STRING_LITERAL;  }

    [Ee][Nn][Tt][Ii][Tt][Yy][Tt][Yy][Pp][Ee]      { return ENTITY_TYPE_KEYWORD; }
    [Cc][Oo][Nn][Cc][Ee][Pp][Tt][Tt][Yy][Pp][Ee]  { return CONCEPT_TYPE_KEYWORD; }
    [Ee][Vv][Ee][Nn][Tt][Tt][Yy][Pp][Ee]          { return EVENT_TYPE_KEYWORD; }
    [Ss][Tt][Aa][Nn][Dd][Aa][Rr][Dd][Tt][Yy][Pp][Ee] { return STANDARD_TYPE_KEYWORD; }
    [Bb][Aa][Ss][Ii][Cc][Tt][Yy][Pp][Ee]          { return BASIC_TYPE_KEYWORD; }
    [Ii][Nn][Tt][Ee][Gg][Ee][Rr]                  { return INTEGER_KEYWORD; }
    [Ff][Ll][Oo][Aa][Tt]  { return FLOAT_KEYWORD; }
    [Tt][Ee][Xx][Tt]      { return TEXT_KEYWORD; }

    {IDENTIFIER} { return IDENTIFIER; }
    {LETTERDIGIT} { return TEXT; }

    "->"    { return RIGHT_ARROW; }
    "("     { return LPARENTH; }
    ")"     { return RPARENTH; }
    ","     { return COMMA; }
    ":"     { return COLON; }
    "."     { return DOT; }

    {ANY_CHAR} {
          return TokenType.BAD_CHARACTER;
      }
}
//-------------------------------------------------------------------------------------------------------------------


//-------------------------------------------------------------------------------------------------------------------
// key-value-state
<KV_STATE> {
    [Dd][Ee][Ss][Cc] { return DESC_KEYWORD; }
    [Pp][Rr][Oo][Pp][Ee][Rr][Tt][Ii][Ee][Ss] { return PROPERTIES_KEYWORD; }
    [Rr][Ee][Ll][Aa][Tt][Ii][Oo][Nn][Ss]     { return RELATIONS_KEYWORD; }
    [Hh][Yy][Pp][Ee][Rr][Nn][Yy][Mm][Pp][Rr][Ee][Dd][Ii][Cc][Aa][Tt][Ee] { return HYPERNYMP_PREDICATE_KEYWORD; }
    [Rr][Ee][Gg][Uu][Ll][Aa][Rr] { return REGULAR_KEYWORD; }
    [Ss][Pp][Rr][Ee][Aa][Dd][Aa][Bb][Ll][Ee] { return SPREADABLE_KEYWORD; }
    [Aa][Uu][Tt][Oo][Rr][Ee][Ll][Aa][Tt][Ee] { return AUTORELATE_KEYWORD; }
    [Cc][Oo][Nn][Ss][Tt][Rr][Aa][Ii][Nn][Tt] { return CONSTRAINT_KEYWORD; }
    [Rr][Uu][Ll][Ee] { return RULE_KEYWORD; }
    [Ii][Nn][Dd][Ee][Xx] { return INDEX_KEYWORD; }

    {IDENTIFIER} { return IDENTIFIER; }

    ":" {
          yybegin(WAITING_VALUE_STATE);
          return COLON;
      }

    {ANY_CHAR} {
          return TokenType.BAD_CHARACTER;
      }
}
//-------------------------------------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------------------------------------
// waiting value
<WAITING_VALUE_STATE> {
    "[[": {
          yybegin(WAITING_BLOCK_VALUE_STATE);
          return DOUBLE_LBRACKET;
      }

    ([Ii][Ss][Aa]) {
          return IS_A_KEYWORD;
      }
    ([Ll][Oo][Cc][Aa][Tt][Ee][Aa][Tt]) {
          return LOCATE_AT_KEYWORD;
      }
    ([Mm][Aa][Nn][Nn][Ee][Rr][Oo][ff]) {
          return MANNER_OF_KEYWORD;
      }
    ([Tt][Ee][Xx][Tt]) {
          return TEXT_KEYWORD;
      }
    ([Vv][Ee][Cc][Tt][Oo][Rr]) {
          return VECTOR_KEYWORD;
      }
    ([Tt][Ee][Xx][Tt][Aa][Nn][Dd][Vv][Ee][Cc][Tt][Oo][Rr]) {
          return TEXT_AND_VECTOR_KEYWORD;
      }
    ([Nn][Oo][Tt][Nn][Uu][Ll][Ll]) {
          return NOT_NULL_KEYWORD;
      }
    ([Mm][Uu][Ll][Tt][Ii][Vv][Aa][Ll][Uu][Ee]) {
          return MULTI_VALUE_KEYWORD;
      }

    [^ \n]+ {
          return TEXT;
      }
}
//-------------------------------------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------------------------------------
// key-value-state
<WAITING_BLOCK_VALUE_STATE> {
    "]]" {
          yybegin(KV_STATE);
          return DOUBLE_RBRACKET;
      }

    ^(.*\]\]) {
          yybegin(WAITING_VALUE_STATE);
          return PLAIN_TEXT;
      }
}
//-------------------------------------------------------------------------------------------------------------------
