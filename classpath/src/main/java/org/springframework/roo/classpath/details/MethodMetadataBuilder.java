package org.springframework.roo.classpath.details;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.support.style.ToStringCreator;

/**
 * Builder for {@link MethodMetadata}.
 * 
 * @author Ben Alex
 * @since 1.1
 */
public class MethodMetadataBuilder extends AbstractInvocableMemberMetadataBuilder<MethodMetadata> {
	
	// Fields
	private JavaSymbolName methodName;
	private JavaType returnType;
	
	public MethodMetadataBuilder(String declaredbyMetadataId) {
		super(declaredbyMetadataId);
	}
	
	public MethodMetadataBuilder(MethodMetadata existing) {
		super(existing);
		init(existing.getMethodName(), existing.getReturnType());
	}
	
	public MethodMetadataBuilder(String declaredbyMetadataId, MethodMetadata existing) {
		super(declaredbyMetadataId, existing);
		init(existing.getMethodName(), existing.getReturnType());
	}

	/**
	 * Constructor for a method with parameters
	 *
	 * @param declaredbyMetadataId
	 * @param modifier
	 * @param methodName
	 * @param returnType
	 * @param parameterTypes
	 * @param parameterNames
	 * @param bodyBuilder
	 */
	public MethodMetadataBuilder(String declaredbyMetadataId, int modifier, JavaSymbolName methodName, JavaType returnType, List<AnnotatedJavaType> parameterTypes, List<JavaSymbolName> parameterNames, InvocableMemberBodyBuilder bodyBuilder) {
		this(declaredbyMetadataId);
		setModifier(modifier);
		setParameterTypes(parameterTypes);
		setParameterNames(parameterNames);
		init(methodName, returnType);
		setBodyBuilder(bodyBuilder);
	}

	/**
	 * Constructor for a method with no parameters
	 *
	 * @param declaredbyMetadataId
	 * @param modifier
	 * @param methodName
	 * @param returnType
	 * @param bodyBuilder
	 */
	public MethodMetadataBuilder(String declaredbyMetadataId, int modifier, JavaSymbolName methodName, JavaType returnType, InvocableMemberBodyBuilder bodyBuilder) {
		this(declaredbyMetadataId, modifier, methodName, returnType, new ArrayList<AnnotatedJavaType>(), new ArrayList<JavaSymbolName>(), bodyBuilder);
	}

	public MethodMetadata build() {
		return new DefaultMethodMetadata(getCustomData().build(), getDeclaredByMetadataId(), getModifier(), buildAnnotations(), getMethodName(), getReturnType(), getParameterTypes(), getParameterNames(), getThrowsTypes(), getBodyBuilder().getOutput());
	}

	private void init(final JavaSymbolName methodName, final JavaType returnType) {
		this.methodName = methodName;
		this.returnType = returnType;
	}

	public JavaSymbolName getMethodName() {
		return methodName;
	}

	public void setMethodName(JavaSymbolName methodName) {
		this.methodName = methodName;
	}

	public JavaType getReturnType() {
		return returnType;
	}

	public void setReturnType(JavaType returnType) {
		this.returnType = returnType;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
			// Append the parts of the method that make up the Java signature
			.append("methodName", methodName)
			.append("parameterTypes", getParameterTypes())
			.toString();
	}
}
