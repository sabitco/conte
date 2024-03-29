package co.org.conte.sgm.entity.activiti;
// Generated 26/06/2013 10:25:20 AM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * ActReProcdef generated by hbm2java
 */
@Entity
@Table(name="ACT_RE_PROCDEF"
    ,catalog="alfresco"
)
public class ActReProcdef  implements java.io.Serializable {


     private String id;
     private String category;
     private String name;
     private String key;
     private Integer version;
     private String deploymentId;
     private String resourceName;
     private String dgrmResourceName;
     private Byte hasStartFormKey;
     private Set<ActRuTask> actRuTasks = new HashSet<ActRuTask>(0);

    public ActReProcdef() {
    }

	
    public ActReProcdef(String id) {
        this.id = id;
    }
    public ActReProcdef(String id, String category, String name, String key, Integer version, String deploymentId, String resourceName, String dgrmResourceName, Byte hasStartFormKey, Set actRuTasks) {
       this.id = id;
       this.category = category;
       this.name = name;
       this.key = key;
       this.version = version;
       this.deploymentId = deploymentId;
       this.resourceName = resourceName;
       this.dgrmResourceName = dgrmResourceName;
       this.hasStartFormKey = hasStartFormKey;
       this.actRuTasks = actRuTasks;
    }
   
     @Id 
    
    @Column(name="ID_", unique=true, nullable=false, length=64)
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name="CATEGORY_")
    public String getCategory() {
        return this.category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    @Column(name="NAME_")
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="KEY_")
    public String getKey() {
        return this.key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    @Column(name="VERSION_")
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    @Column(name="DEPLOYMENT_ID_", length=64)
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    @Column(name="RESOURCE_NAME_", length=4000)
    public String getResourceName() {
        return this.resourceName;
    }
    
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    
    @Column(name="DGRM_RESOURCE_NAME_", length=4000)
    public String getDgrmResourceName() {
        return this.dgrmResourceName;
    }
    
    public void setDgrmResourceName(String dgrmResourceName) {
        this.dgrmResourceName = dgrmResourceName;
    }
    
    @Column(name="HAS_START_FORM_KEY_")
    public Byte getHasStartFormKey() {
        return this.hasStartFormKey;
    }
    
    public void setHasStartFormKey(Byte hasStartFormKey) {
        this.hasStartFormKey = hasStartFormKey;
    }
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="actReProcdef")
    public Set<ActRuTask> getActRuTasks() {
        return this.actRuTasks;
    }
    
    public void setActRuTasks(Set<ActRuTask> actRuTasks) {
        this.actRuTasks = actRuTasks;
    }
}