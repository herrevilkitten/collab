package org.evilkitten.gitboard.application.services.whiteboard;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.typesafe.config.Config;
import org.nnsoft.guice.guartz.Scheduled;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Scheduled(jobName = "whiteboardSaver", cronExpression = "0 0/5 * * * ?")
public class WhiteboardTasks implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(WhiteboardTasks.class);

    private Whiteboard whiteboard;
    private Config config;

    @Inject
    public WhiteboardTasks(Whiteboard whiteboard, Config config) {
        this.whiteboard = whiteboard;
        LOG.info("Creating WhiteboardTasks");
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("Executing WhiteboardTasks");
    }
}
