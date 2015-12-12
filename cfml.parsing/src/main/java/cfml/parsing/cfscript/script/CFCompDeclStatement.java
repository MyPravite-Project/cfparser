package cfml.parsing.cfscript.script;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.antlr.v4.runtime.Token;

import cfml.parsing.cfscript.CFContext;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFLiteral;
import cfml.parsing.reporting.ParseException;

public class CFCompDeclStatement extends CFParsedStatement {
	
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> attributes;
	private CFScriptStatement body;
	
	private byte access;
	
	public byte getAccess() {
		return access;
	}
	
	public String getReturnType() {
		return returnType;
	}
	
	private String returnType;
	
	// TODO: prevent function declared inside function. May want to do this elsewhere
	public CFCompDeclStatement(Token _t, Map<CFExpression, CFExpression> _attr, CFScriptStatement _body) {
		super(_t);
		body = _body;
		// handle the function attributes
		attributes = new HashMap<String, String>();
		if (_attr != null) {
			Iterator<CFExpression> keys = _attr.keySet().iterator();
			while (keys.hasNext()) {
				String nextKey = keys.next().Decompile(0);
				CFExpression nextExpr = _attr.get(nextKey);
				if (!(nextExpr instanceof CFLiteral)) {
					throw new ParseException(_t,
							"The attribute " + nextKey.toUpperCase() + " must have a constant value");
				}
				
				attributes.put(nextKey, ((CFLiteral) nextExpr).getStringImage());
			}
		}
		
	}
	
	public CFScriptStatement getBody() {
		return body;
	}
	
	@Override
	public void checkIndirectAssignments(String[] scriptSource) {
		body.checkIndirectAssignments(scriptSource);
	}
	
	public CFStatementResult Exec(CFContext context) {
		return null;
	}
	
	@Override
	public String Decompile(int indent) {
		StringBuilder sb = new StringBuilder();
		sb.append(Indent(indent));
		sb.append(" component ");
		sb.append(body.Decompile(indent + 2));
		sb.append("\n");
		sb.append(Indent(indent));
		sb.append("}");
		
		return sb.toString();
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
}
