package com.siebre.smf;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.siebre.agreement.AgreementSpecImpl;
import com.siebre.product.InsuranceProductImpl;
import com.siebre.product.ProductComponentImpl;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * WARNING: This is duplicate class definition that use java class loader mechanism to override somewhere else. This
 * class should be removed in the future if original changes has been made.
 *
 * @author ZhangChi
 * @since 2013-4-8
 */
@MappedSuperclass
public class SmfBehaviorImpl extends SmfModelElementImpl {
	
	private String internalReference;
	
	@Any(metaColumn = @Column(name = "parentSmfClassType"))
	@AnyMetaDef(idType = "long", metaType = "string", metaValues = {
			@MetaValue(value = "A", targetEntity = AgreementSpecImpl.class),
			@MetaValue(value = "P", targetEntity = ProductComponentImpl.class),
			@MetaValue(value = "M", targetEntity = InsuranceProductImpl.class),
			@MetaValue(value = "R", targetEntity = SmfRoleImpl.class),
			@MetaValue(value = "T", targetEntity = SmfRequestImpl.class)
	})
	@JoinColumn(name = "parentSmfClassId")
	private SmfClass parentSmfClass;

	@Lob
	@Column(length = 65535)
	private String body;
	
	@Transient
	private ScriptFormatter formatter = new SimpleScriptFormatter();

	public String getInternalReference() {
		return internalReference;
	}

	public void setInternalReference(String internalReference) {
		this.internalReference = internalReference;
	}

	public SmfClass getParentSmfClass() {
		return parentSmfClass;
	}

	public void setParentSmfClass(SmfClass parentSmfClass) {
		this.parentSmfClass = parentSmfClass;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		if (body != null)
			this.body = formatter.format(body);
	}

	static interface ScriptFormatter {
		
		String format(String script);
	}
	
	static class SimpleScriptFormatter implements ScriptFormatter {
		
		protected static final Logger LOGGER = LoggerFactory.getLogger(SimpleScriptFormatter.class);

		public String format(String script) {
			StringBuilder sb = new StringBuilder();
			
			try {
				List<String> lines = IOUtils.readLines(new StringReader(script));
				if (lines.isEmpty()) {
					return "";
				}
				
				int lineNum = 0;

				//handle first line if it is not blank
				String firstLine = lines.get(0);
				if (StringUtils.isNotBlank(firstLine)) {
					sb.append(firstLine.trim());
					lineNum = 1;
				}
				
				String leadingBlank = getLeadingBlank(lines);
				LOGGER.trace("leading blank is: '{}'", leadingBlank);
				
				//ignore empty header lines
				while (lineNum < lines.size()) {
					String line = lines.get(lineNum);
					if (!StringUtils.isBlank(line))
						break;
					lineNum++;
				}
				
				//handle other lines
				while (lineNum < lines.size()) {
					String line = lines.get(lineNum);
					
					if (sb.length() > 0) {
						sb.append('\n');
					}
					
					if (StringUtils.isNotBlank(line)) {
						line = StringUtils.removeStart(line, leadingBlank);
						sb.append(StringUtils.stripEnd(line, null));
					}
					
					lineNum++;
				}
			} catch (IOException e) {
			}

			return sb.toString();
		}

		private String getLeadingBlank(List<String> lines) {
			if (lines.size() == 1) {
				LOGGER.trace("leading blank is '' for one line script");
				return "";
			} else if (lines.size() == 2) {
				String firstLine = lines.get(0).trim();
				if (firstLine.startsWith("if")
					|| firstLine.startsWith("for")
					|| firstLine.startsWith("when")) {
					return "";
				} else {
					return getLeadingBlank(lines.get(1));
				}
			}
			List<String> nonEmptyLines = ImmutableList.copyOf(Iterables.filter(lines, new Predicate<String>() {

				public boolean apply(@Nullable String input) {
					return StringUtils.isNotBlank(input);
				}
			}));
			
			String result = StringUtils.getCommonPrefix(nonEmptyLines.subList(1, nonEmptyLines.size()).toArray(new String[0]));
			return StringUtils.isBlank(result) ? result : getLeadingBlank(result);
		}

		private String getLeadingBlank(String line) {
			StringBuilder sb = new StringBuilder();
			for (char c : line.toCharArray()) {
				if (!Character.isWhitespace(c)) {
					break;
				}
				sb.append(c);
			}
			return sb.toString();
		}
	}
	
	static class ComplexScriptFormatter implements ScriptFormatter {

		public String format(String script) {
			StringBuilder sb = new StringBuilder();
			
			int indentLevel = 0;
			try {
				List<String> lines = IOUtils.readLines(new StringReader(script));
				
				int lineNum = 0;
				FormattingContext context = new FormattingContext();
				for (String line : lines) {
					sb.append(StringUtils.repeat('\t', indentLevel)).append(line.trim());
					if (lineNum < lines.size() - 1) {
						sb.append('\n');
					}

					context.setCurrentLine(line);
					
					if (isBlockStart(context)) {
						indentLevel++;
					} else if (isBlockEnd(context)) {
						indentLevel--;
					}

					lineNum++;
				}
			} catch (IOException e) {
			}

			return sb.toString();
		}
		
		private static final Pattern IF_STATEMENT = Pattern.compile("if ?(.+)");

		private boolean isBlockStart(FormattingContext context) {
			String line = context.getCurrentLine();
			boolean result = IF_STATEMENT.matcher(line).find();
			context.setOneLineBlock(!line.endsWith("{"));
			return result;
		}

		private boolean isBlockEnd(FormattingContext context) {
			String line = context.getCurrentLine();
			boolean result = line.endsWith("}");
			if (!result && context.isOneLineBlock())
				result = true;
			if (result)
				context.blockEnd();
			return result;
		}
		
		static class FormattingContext {
			
			private String currentLine;
			
			private boolean oneLineBlock = false;

			public String getCurrentLine() {
				return currentLine;
			}

			public void blockEnd() {
				oneLineBlock = false;
			}

			public void setCurrentLine(String currentLine) {
				this.currentLine = currentLine;
			}

			public boolean isOneLineBlock() {
				return oneLineBlock;
			}

			public void setOneLineBlock(boolean oneLineBlock) {
				this.oneLineBlock = oneLineBlock;
			}
		}
		
	}
}
