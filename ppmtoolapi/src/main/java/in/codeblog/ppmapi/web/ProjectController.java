package in.codeblog.ppmapi.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import in.codeblog.ppmapi.domain.Project;
import in.codeblog.ppmapi.service.MapValidationErrorService;
import in.codeblog.ppmapi.service.ProjectService;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@PostMapping("")
	public ResponseEntity<?> saveProject(@Valid @RequestBody Project project, BindingResult result) {
		/*
		 * 
		 Case 01:> if (result.hasErrors()) { return new
		 ResponseEntity<String>("Invalid Project Object", HttpStatus.BAD_REQUEST); }
		

		
		 Case 02:> if (result.hasErrors()) { return new
		  ResponseEntity<List<FieldError>>(result.getFieldErrors(),HttpStatus.
		  BAD_REQUEST); }
		 
		
		case 03:>
		if (result.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for (FieldError error : result.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
		}

        case 04:> is given below
		 */
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null)
			return errorMap;
		Project proj = projectService.saveOrUpdateProject(project);
		return new ResponseEntity<Project>(proj, HttpStatus.CREATED);
	}

	@GetMapping("/{projectId}")
	public ResponseEntity<?> getProjectByIdentifier(@PathVariable String projectId) {
		Project project = projectService.findProjectByIdentifier(projectId);
		return new ResponseEntity<Project>(project, HttpStatus.OK);
	}

	@DeleteMapping("/{projectId}")
	public ResponseEntity<?> deleteProject(@PathVariable String projectId) {
		projectService.deleteProjectByIdentifier(projectId);
		return new ResponseEntity<String>("Project with ID:'" + projectId + "' was deleted", HttpStatus.OK);
	}

	@GetMapping("/all")
	public Iterable<Project> getAllProjects() {
		return projectService.findAllProjcts();
	}

}
