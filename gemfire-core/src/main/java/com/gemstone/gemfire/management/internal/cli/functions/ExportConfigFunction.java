/*=========================================================================
 * Copyright (c) 2010-2014 Pivotal Software, Inc. All Rights Reserved.
 * This product is protected by U.S. and international copyright
 * and intellectual property laws. Pivotal products are covered by
 * one or more patents listed at http://www.pivotal.io/patents.
 *=========================================================================
 */
package com.gemstone.gemfire.management.internal.cli.functions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.gemstone.gemfire.SystemFailure;
import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheClosedException;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.distributed.DistributedMember;
import com.gemstone.gemfire.distributed.internal.DistributionConfigImpl;
import com.gemstone.gemfire.distributed.internal.InternalDistributedSystem;
import com.gemstone.gemfire.internal.ConfigSource;
import com.gemstone.gemfire.internal.InternalEntity;
import com.gemstone.gemfire.internal.cache.xmlcache.CacheXmlGenerator;
import com.gemstone.gemfire.internal.logging.LogService;

public class ExportConfigFunction implements Function, InternalEntity {
  private static final Logger logger = LogService.getLogger();
  
  public static final String ID = ExportConfigFunction.class.getName();

  private static final long serialVersionUID = 1L;

  @Override
  public void execute(FunctionContext context) {
    // Declared here so that it's available when returning a Throwable
    String memberId = "";

    try {
      Cache cache = CacheFactory.getAnyInstance();
      DistributedMember member = cache.getDistributedSystem().getDistributedMember();

      memberId = member.getId();
      // If they set a name use it instead
      if (!member.getName().equals("")) {
        memberId = member.getName();
      }

      // Generate the cache XML
      StringWriter xmlWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(xmlWriter);
      CacheXmlGenerator.generate(cache, printWriter, true, false, false);
      printWriter.close();
      
      // Generate the properties file
      DistributionConfigImpl  config = (DistributionConfigImpl) ((InternalDistributedSystem) cache.getDistributedSystem()).getConfig();
      StringBuffer propStringBuf = new StringBuffer();
      String lineSeparator = System.getProperty("line.separator");
      for (Map.Entry entry : config.getConfigPropsFromSource(ConfigSource.runtime()).entrySet()) {
        if (entry.getValue() != null && !entry.getValue().equals("")) {
          propStringBuf.append(entry.getKey()).append("=").append(entry.getValue()).append(lineSeparator);
        }
      }
      for (Map.Entry entry : config.getConfigPropsFromSource(ConfigSource.api()).entrySet()) {
        if (entry.getValue() != null && !entry.getValue().equals("")) {
          propStringBuf.append(entry.getKey()).append("=").append(entry.getValue()).append(lineSeparator);
        }
      }
      for (Map.Entry entry : config.getConfigPropsDefinedUsingFiles().entrySet()) {
        if (entry.getValue() != null && !entry.getValue().equals("")) {
          propStringBuf.append(entry.getKey()).append("=").append(entry.getValue()).append(lineSeparator);
        }
      }
      // fix for bug 46653
      for (Map.Entry entry : config.getConfigPropsFromSource(ConfigSource.launcher()).entrySet()) {
        if (entry.getValue() != null && !entry.getValue().equals("")) {
          propStringBuf.append(entry.getKey()).append("=").append(entry.getValue()).append(lineSeparator);
        }
      }
      
      CliFunctionResult result = new CliFunctionResult(memberId, new String[] { xmlWriter.toString(), propStringBuf.toString() });

      context.getResultSender().lastResult(result);
      
    } catch (CacheClosedException cce) {
      CliFunctionResult result = new CliFunctionResult(memberId, false, null);
      context.getResultSender().lastResult(result);
      
    } catch (VirtualMachineError e) {
      SystemFailure.initiateFailure(e);
      throw e;
      
    } catch (Throwable th) {
      SystemFailure.checkFailure();
      logger.error("Could not export config {}", th.getMessage(), th);
      CliFunctionResult result = new CliFunctionResult(memberId, th, null);
      context.getResultSender().lastResult(result);
    }
  }

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public boolean hasResult() {
    return true;
  }

  @Override
  public boolean optimizeForWrite() {
    return false;
  }

  @Override
  public boolean isHA() {
    return false;
  }
}