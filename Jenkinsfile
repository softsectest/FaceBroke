node {
	git url: 'git@github.com:softwaresecured/FaceBroke.git', credentialsId: 'jarusk'

	sh "git rev-parse HEAD > .git/commit-id"
    def commit_id = readFile('.git/commit-id').trim()
    println commit_id
    
    stage "build"
    def app = docker.build("FaceBroke")
}
