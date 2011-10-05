package org.springframework.roo.addon.solr;

import static org.springframework.roo.model.RooJavaType.ROO_SOLR_WEB_SEARCHABLE;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.addon.web.mvc.controller.scaffold.WebScaffoldMetadata;
import org.springframework.roo.addon.web.mvc.controller.scaffold.WebScaffoldMetadataProvider;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.itd.AbstractItdMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;
import org.springframework.roo.support.util.Assert;
/**
 * Provides {@link SolrWebSearchMetadata}.
 * 
 * @author Stefan Schmidt
 * @since 1.1
 */
@Component(immediate = true)
@Service
public class SolrWebSearchMetadataProvider extends AbstractItdMetadataProvider {
	
	// Fields
	@Reference private WebScaffoldMetadataProvider webScaffoldMetadataProvider;

	protected void activate(ComponentContext context) {
		metadataDependencyRegistry.registerDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
		webScaffoldMetadataProvider.addMetadataTrigger(ROO_SOLR_WEB_SEARCHABLE);
		addMetadataTrigger(ROO_SOLR_WEB_SEARCHABLE);	
	}

	/**
	 * OSGi bundle deactivation callback
	 * 
	 * @param context
	 * @since 1.2.0
	 */
	protected void deactivate(ComponentContext context) {
		metadataDependencyRegistry.deregisterDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
		webScaffoldMetadataProvider.removeMetadataTrigger(ROO_SOLR_WEB_SEARCHABLE);
		removeMetadataTrigger(ROO_SOLR_WEB_SEARCHABLE);	
	}
	
	protected ItdTypeDetailsProvidingMetadataItem getMetadata(String metadataIdentificationString, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata, String itdFilename) {
		// We need to parse the annotation, which we expect to be present
		SolrWebSearchAnnotationValues annotationValues = new SolrWebSearchAnnotationValues(governorPhysicalTypeMetadata);
		if (!annotationValues.isAnnotationFound() || annotationValues.searchMethod == null) {
			return null;
		}
		
		// Acquire bean info (we need getters details, specifically)
		JavaType javaType = SolrWebSearchMetadata.getJavaType(metadataIdentificationString);
		Path path = SolrWebSearchMetadata.getPath(metadataIdentificationString);
		String webScaffoldMetadataKey = WebScaffoldMetadata.createIdentifier(javaType, path);
		
		// We want to be notified if the getter info changes in any way 
		metadataDependencyRegistry.registerDependency(webScaffoldMetadataKey, metadataIdentificationString);
		WebScaffoldMetadata webScaffoldMetadata = (WebScaffoldMetadata) metadataService.get(webScaffoldMetadataKey);
		
		// Abort if we don't have getter information available
		if (webScaffoldMetadata == null || !webScaffoldMetadata.isValid()) {
			return null;
		}
		
		JavaType targetObject = webScaffoldMetadata.getAnnotationValues().getFormBackingObject();
		Assert.notNull(targetObject, "Could not aquire form backing object for the '" + WebScaffoldMetadata.getJavaType(webScaffoldMetadata.getId()).getFullyQualifiedTypeName() + "' controller");
		
		SolrMetadata solrMetadata = (SolrMetadata) metadataService.get(SolrMetadata.createIdentifier(targetObject, Path.SRC_MAIN_JAVA));
		Assert.notNull(solrMetadata, "Could not determine SolrMetadata for type '" + targetObject.getFullyQualifiedTypeName() + "'");

		// Otherwise go off and create the to String metadata
		return new SolrWebSearchMetadata(metadataIdentificationString, aspectName, governorPhysicalTypeMetadata, annotationValues, webScaffoldMetadata.getAnnotationValues(), solrMetadata.getAnnotationValues());
	}
	
	public String getItdUniquenessFilenameSuffix() {
		return "SolrWebSearch";
	}

	protected String getGovernorPhysicalTypeIdentifier(String metadataIdentificationString) {
		JavaType javaType = SolrWebSearchMetadata.getJavaType(metadataIdentificationString);
		Path path = SolrWebSearchMetadata.getPath(metadataIdentificationString);
		return PhysicalTypeIdentifier.createIdentifier(javaType, path);
	}
	
	protected String createLocalIdentifier(JavaType javaType, Path path) {
		return SolrWebSearchMetadata.createIdentifier(javaType, path);
	}

	public String getProvidesType() {
		return SolrWebSearchMetadata.getMetadataIdentiferType();
	}
}