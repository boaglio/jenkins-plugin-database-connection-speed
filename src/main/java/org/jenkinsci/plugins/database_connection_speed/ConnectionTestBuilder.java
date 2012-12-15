package org.jenkinsci.plugins.database_connection_speed;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.jenkinsci.plugins.database_connection_speed.db.TestConnection;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * @author Fernando Boaglio
 */
public class ConnectionTestBuilder extends Builder {

	private final String dbType;
	private final String url;
	private final String usuario;
	private final String senha;

	@DataBoundConstructor
	public ConnectionTestBuilder(String dbType,String url,String usuario,String senha) {
		this.dbType = dbType;
		this.url = url;
		this.usuario = usuario;
		this.senha = senha;
	}

	public String getUrl() {
		return url;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getSenha() {
		return senha;
	}

	public String getDbType() {
		return dbType;
	}

	@Override
	public boolean perform(AbstractBuild build,Launcher launcher,BuildListener listener) {

		try {
			TestConnection.inicia(url,usuario,senha,dbType,getDescriptor().getDetalhes(),listener);
			listener.getLogger().println(" Tempo: " + TestConnection.diff() + " milisegundos  ");
			TestConnection.finaliza();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

		private boolean detalhes;

		public FormValidation doCheckName(@QueryParameter String value) throws IOException,ServletException {
			if (value.length() == 0) { return FormValidation.error("Informe o campo"); }
			if (value.length() < 4) { return FormValidation.warning("Valor do campo muito curto"); }
			return FormValidation.ok();
		}

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return "Testar o acesso ao banco de dados";
		}

		@Override
		public boolean configure(StaplerRequest req,JSONObject formData) throws FormException {
			detalhes = formData.getBoolean("detalhes");
			save();
			return super.configure(req,formData);
		}

		public boolean getDetalhes() {
			return detalhes;
		}
	}
}
