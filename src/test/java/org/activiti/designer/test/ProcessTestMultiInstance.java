package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestMultiInstance {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/main/resources/diagrams/MultiInstance.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("process1.bpmn20.xml", new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process1", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		TaskService taskService = activitiRule.getTaskService();
		assertEquals(3, taskService.createTaskQuery().taskAssignee("henryyan").count());
		Task task = taskService.createTaskQuery().taskAssignee("henryyan").listPage(0, 1).get(0);
		taskService.complete(task.getId());
		
		task = taskService.createTaskQuery().taskAssignee("henryyan").listPage(0, 1).get(0);
		taskService.complete(task.getId());
	}
}