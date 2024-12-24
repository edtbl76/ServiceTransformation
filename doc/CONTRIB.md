# Contents

- [Coding](#coding)
- [Commits](#commit-messages)
- [Building](#building)
- [Releasing](#tag-and-release)

---
## Coding

- all code is written on develop
- when making changes, create a branch from develop:
  - dev-<name>-<description> for feature work
  - fix-<name>-<description> for fixes

- PRs must be reviewed by me! @edtbl76

## Commit Messages
1. All commit messages should be headed w/ the working feature: 
2. Each section should be bulleted w/ the component being updated
3. Sub-bullets should be a concise description of changes
4. use markdown tics to highlight proper nouns, library names, classes, etc. 

see latest [Release Notes](./RELEASE.md) for inspiration.

## Building

- feature/fixes are PR'd into develop
- develop is PR'd into main

all PRs should be `squash commits`

### Run your tests!!! We don't have CI :) 

## Tag and Release

Once a release is deemed "complete"

### Squash Commits

I do this in Intellij via the (Alt-9 Git tool) Select the commits for the release and squash them. 
- update the commit message to fit [commit format](#commit-messages). 
- push the code to origin/develop

### Create PR

(If you do the push in Intellij, you'll be prompted to create a PR, go ahead and do this)

### Merge PR

- You can do this in Intellij via the Commit (Alt-0) tool, when you checkout `main`
- You can do this from GitHub as well. 

### Tag

I prefer to do this from the CLI
```shell
git checkout main
git tag v2.2.0 -m "feature: Persistence"
git push --tags
```

### Create Release in Github

