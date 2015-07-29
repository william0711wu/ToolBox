package com.duowan.${projectNameInPackage};

import org.junit.Ignore;
import com.duowan.leopard.test.jetty.JettyServer;

@Ignore
public class JettyTest {

	public static void main(String[] args) throws Exception {
		JettyServer.start(8081);
	}

}
