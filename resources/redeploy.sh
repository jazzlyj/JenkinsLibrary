#!/bin/bash -eu

appname=$1
kubeconfig=$2
namespace=$3

dt=$(date '+%d/%m/%Y %H:%M:%S')

echo "redeploy.sh appname:${appname} kubeconfig:${kubeconfig} namespace:${namespace} deployment.yml"

echo "Stack: ${stack_name} namespace: ${namespace}" 1>&2

kubectl --kubeconfig ${kubeconfig} -n ${namespace} patch deployment ${appname} -p "{\"spec\":{\"template\":{\"metadata\":{\"annotations\":{\"jenkins_build_tag\":\"${BUILD_TAG}\", \"build_date\":\"${dt}\"}}}}}"